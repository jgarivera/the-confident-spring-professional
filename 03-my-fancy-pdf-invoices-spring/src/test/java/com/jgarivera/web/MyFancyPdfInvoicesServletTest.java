package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.model.Invoice;
import com.jgarivera.service.InvoiceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// Poor man's Spring test...
public class MyFancyPdfInvoicesServletTest {

    private InvoiceService invoiceService;
    private ObjectMapper objectMapper;
    private MyFancyPdfInvoicesServlet servlet;

    @BeforeEach
    void setUp() throws ServletException {
        servlet = new MyFancyPdfInvoicesServlet();

        // Manually call init method so the Spring application context is created
        servlet.init();

        // Access the beans directly from the servlet for now so we can test
        invoiceService = servlet.getInvoiceService();
        objectMapper = servlet.getObjectMapper();
    }

    @Test
    void it_retrieves_empty_invoices() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invoices");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        String transactionsJson = objectMapper.writeValueAsString(invoiceService.findAll());

        assertEquals(transactionsJson, stringWriter.toString());
    }

    @Test
    void it_retrieves_invoices() throws IOException {
        invoiceService.create("arthur", 1999);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invoices");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        String transactionsJson = objectMapper.writeValueAsString(invoiceService.findAll());

        assertEquals(transactionsJson, stringWriter.toString());
    }

    @Test
    void it_retrieves_dummy_invoices_when_dev() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invoices");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        String transactionsJson = objectMapper.writeValueAsString(invoiceService.findAll());

        assertEquals(transactionsJson, stringWriter.toString());
    }

    @Test
    void it_creates_invoices() throws IOException {
        int currentInvoiceCount = invoiceService.findAll().size();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invoices");
        when(request.getParameter("user_id")).thenReturn("bob");
        when(request.getParameter("amount")).thenReturn("2000");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        // Invoices size assertion is no longer hard-coded to an expected value of `1`
        // When the `dev` profile is active, DummyInvoiceServiceLoader inserts three invoices
        // which messes up the previously hard-coded assertion
        List<Invoice> invoices = invoiceService.findAll();
        assertEquals(currentInvoiceCount + 1, invoices.size());

        Invoice invoice = invoices.get(invoices.size() - 1);

        assertNotNull(invoice.getId());
        assertNotNull(invoice.getPdfUrl());
        assertEquals("bob", invoice.getUserId());
        assertEquals(2000, invoice.getAmount());

        writer.flush();
    }
}
