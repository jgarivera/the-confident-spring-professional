package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.context.ApplicationConfiguration;
import com.jgarivera.model.Invoice;
import com.jgarivera.service.InvoiceService;
import jakarta.servlet.ServletException;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use Spring TestContext framework since we are no longer poor!
@ExtendWith(SpringExtension.class)

// Load our application configuration into this test
@ContextConfiguration(classes = ApplicationConfiguration.class)
@WebAppConfiguration

// Reset application context after each test method executes

// This is important because we do not want the outcome of a test
// method affect another test method

// For example, the invoices created from a test method can affect
// the assertions of a test method that checks invoice count
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InvoicesControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws ServletException {
        // Get all web application beans and make them available in this test
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
                .andExpect(jsonPath("$[0].amount", is(1999)));
    }

    @Test
    void it_creates_invoices() throws Exception {
        int currentInvoiceCount = invoiceService.findAll().size();

        RequestBuilder request = post("/invoices")
                .accept(MediaType.APPLICATION_JSON)
                .param("user_id", "bob")
                .param("amount", "10");

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
