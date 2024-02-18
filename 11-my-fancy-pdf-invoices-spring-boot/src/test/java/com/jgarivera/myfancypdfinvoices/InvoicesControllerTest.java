package com.jgarivera.myfancypdfinvoices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.myfancypdfinvoices.dto.InvoiceDto;
import com.jgarivera.myfancypdfinvoices.model.Invoice;
import com.jgarivera.myfancypdfinvoices.service.InvoiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Creates a mock web environment by default
// This allows us to access a WebApplicationContext in our test
@SpringBootTest

// Instead of creating MockMvc from a test setup method, we just annotate our test class
@AutoConfigureMockMvc
class InvoicesControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void it_retrieves_empty_invoices() throws Exception {
        RequestBuilder request = get("/invoices")
                .accept(MediaType.APPLICATION_JSON);

        // Had to install `hamcrest` and `json-path` dependencies
        // to make JSON path assertions work...
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void it_retrieves_invoices() throws Exception {
        invoiceService.create("arthur", 1999);

        RequestBuilder request = get("/invoices")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_id", is("arthur")))
                .andExpect(jsonPath("$[0].amount", is(1999)))
                .andExpect(jsonPath("$[0].pdf_url", notNullValue()));
    }

    @Test
    void it_creates_invoices() throws Exception {
        int currentInvoiceCount = invoiceService.findAll().size();

        var invoiceDto = new InvoiceDto();
        invoiceDto.setUserId("bob");
        invoiceDto.setAmount(10);

        String requestBody = objectMapper.writeValueAsString(invoiceDto);

        RequestBuilder request = post("/invoices")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        List<Invoice> invoices = invoiceService.findAll();
        assertEquals(currentInvoiceCount + 1, invoices.size());

        Invoice createdInvoice = invoices.get(invoices.size() - 1);

        assertNotNull(createdInvoice.getId());
        assertNotNull(createdInvoice.getPdfUrl());
        assertEquals("bob", createdInvoice.getUserId());
        assertEquals(10, createdInvoice.getAmount());
    }

    @Test
    void it_validates_invoices_user_id() throws Exception {
        mockMvc.perform(post("/invoices")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("user_id", " ")
                        .param("amount", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void it_validates_invoices_amount() throws Exception {
        mockMvc.perform(post("/invoices")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("user_id", "bob")
                        .param("amount", "invalid-number"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/invoices")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("user_id", "bob")
                        .param("amount", "99"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/invoices")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("user_id", "bob")
                        .param("amount", "1"))
                .andExpect(status().isBadRequest());
    }
}
