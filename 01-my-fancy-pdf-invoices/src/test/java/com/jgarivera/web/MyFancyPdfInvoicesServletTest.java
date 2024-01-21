package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.model.Invoice;
import com.jgarivera.service.InvoiceService;
import com.jgarivera.service.UserService;
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

public class MyFancyPdfInvoicesServletTest {

    private InvoiceService invoiceService;
    private MyFancyPdfInvoicesServlet servlet;

    @BeforeEach
    void setUp() {
        UserService userService = new UserService();
        ObjectMapper objectMapper = new ObjectMapper();

        invoiceService = new InvoiceService(userService);
        servlet = new MyFancyPdfInvoicesServlet(invoiceService, objectMapper);
    }

    @Test
    void it_creates_invoices() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/invoices");
        when(request.getParameter("user_id")).thenReturn("bob");
        when(request.getParameter("amount")).thenReturn("2000");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        List<Invoice> invoices = invoiceService.findAll();
        assertEquals(1, invoices.size());

        Invoice invoice = invoices.get(0);

        assertNotNull(invoice.getId());
        assertNotNull(invoice.getPdfUrl());
        assertEquals("bob", invoice.getUserId());
        assertEquals(2000, invoice.getAmount());

        writer.flush();
    }
}
