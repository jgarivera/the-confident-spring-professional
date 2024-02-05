package com.jgarivera.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.context.ApplicationConfiguration;
import com.jgarivera.dto.TransactionDto;
import com.jgarivera.model.Transaction;
import com.jgarivera.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TransactionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
        transactionService.create(new BigDecimal("1000"), "billy");

        RequestBuilder request = get("/transactions")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount", is(1000)))
                .andExpect(jsonPath("$[0].reference", is("billy")));
    }

    @Test
    void it_creates_transactions() throws Exception {
        int currentTransactionCount = transactionService.findAll().size();

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(new BigDecimal("19100.25"), "michael")))
                .andExpect(status().isCreated());

        List<Transaction> transactions = transactionService.findAll();
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
                        .content(makeCreateTransactionRequestBody(new BigDecimal("-1"), "isabel")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("amount")));

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(null, "isabel")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("amount")));
    }

    @Test
    void it_validates_transactions_reference() throws Exception {
        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(new BigDecimal("100"), "")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("reference")));

        mockMvc.perform(post("/transactions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeCreateTransactionRequestBody(new BigDecimal("100"), null)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field", is("reference")));
    }

    private String makeCreateTransactionRequestBody(BigDecimal amount, String reference) throws JsonProcessingException {
        TransactionDto transactionDto = new TransactionDto(amount, reference);
        return objectMapper.writeValueAsString(transactionDto);
    }
}
