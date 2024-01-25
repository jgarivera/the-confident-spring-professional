package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.model.Transaction;
import com.jgarivera.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyBankServletTest {

    private TransactionService transactionService;
    private ObjectMapper objectMapper;
    private MyBankServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new MyBankServlet();
        servlet.init();

        transactionService = servlet.getTransactionService();
        objectMapper = servlet.getObjectMapper();
    }

    @Test
    void it_retrieves_empty_transactions() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/transactions");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        String transactionsJson = objectMapper.writeValueAsString(transactionService.findAll());

        assertEquals(transactionsJson, stringWriter.toString());
    }

    @Test
    void it_retrieves_transactions() throws IOException {
        transactionService.create(new BigDecimal("1000"), "billy");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/transactions");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        String transactionsJson = objectMapper.writeValueAsString(transactionService.findAll());

        assertEquals(transactionsJson, stringWriter.toString());
    }

    @Test
    void it_creates_transactions() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/transactions");
        when(request.getParameter("amount")).thenReturn("19100.25");
        when(request.getParameter("reference")).thenReturn("michael");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        List<Transaction> invoices = transactionService.findAll();
        assertEquals(1, invoices.size());

        Transaction invoice = invoices.get(0);

        assertNotNull(invoice.getId());
        assertNotNull(invoice.getTimestamp());
        assertEquals(new BigDecimal("19100.25"), invoice.getAmount());
        assertEquals("michael", invoice.getReference());
        assertEquals("We find ways to secure your money (hopefully)", invoice.getSlogan());

        writer.flush();
    }
}
