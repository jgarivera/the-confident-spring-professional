package com.jgarivera.mybank.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.mybank.dto.TransactionDto;
import com.jgarivera.mybank.model.Transaction;
import com.jgarivera.mybank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void it_retrieves_empty_transactions() throws Exception {
        RequestBuilder request = get("/transactions")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void it_retrieves_transactions() throws Exception {
        transactionService.create(new BigDecimal("1000"), "billy", "joe");

        RequestBuilder request = get("/transactions")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(1000.0)))
                .andExpect(jsonPath("$[0].reference", is("billy")));
    }

    @Test
    void it_creates_transactions() throws Exception {
        int currentTransactionCount = iterableToList(transactionService.findAll()).size();

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("19100.25"), "michael", "frank")))
                .andExpect(status().isCreated());

        List<Transaction> transactions = iterableToList(transactionService.findAll());
        Transaction createdTransaction = transactions.get(0);

        assertNotNull(createdTransaction.getId());
        assertNotNull(createdTransaction.getTimestamp());

        assertEquals(currentTransactionCount + 1, transactions.size());
        assertEquals(new BigDecimal("19100.25"), createdTransaction.getAmount());
        assertEquals("michael", createdTransaction.getReference());
        assertEquals("We find ways to secure your money (hopefully)", createdTransaction.getSlogan());
    }

    @Test
    void it_validates_transactions_amount() throws Exception {
        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("-1"), "isabel", "mica")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("amount")));

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(null, "isabel", "mica")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("amount")));
    }

    @Test
    void it_validates_transactions_reference() throws Exception {
        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("100"), "", "bob")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("reference")));

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("100"), null, "bob")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("reference")));
    }

    @Test
    void it_validates_transactions_receiving_user_id() throws Exception {
        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("100"), "john", "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("receivingUserId")));

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(
                                new BigDecimal("100"), "john", null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("receivingUserId")));
    }

    private String makeCreateTransactionRequestBody(BigDecimal amount, String reference, String receivingUserId) throws JsonProcessingException {
        TransactionDto transactionDto = new TransactionDto(amount, reference, receivingUserId);
        return objectMapper.writeValueAsString(transactionDto);
    }

    private <T> List<T> iterableToList(Iterable<T> iterable) {
        List<T> target = new ArrayList<>();
        iterable.forEach(target::add);
        return target;
    }
}
