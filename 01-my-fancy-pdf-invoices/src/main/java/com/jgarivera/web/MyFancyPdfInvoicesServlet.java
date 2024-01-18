package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.service.InvoiceService;
import com.jgarivera.model.Invoice;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MyFancyPdfInvoicesServlet extends HttpServlet {

    private final InvoiceService invoiceService;
    private final ObjectMapper objectMapper;

    public MyFancyPdfInvoicesServlet(InvoiceService invoiceService, ObjectMapper objectMapper) {
        this.invoiceService = invoiceService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String requestUri = request.getRequestURI();

        if (requestUri.equalsIgnoreCase("/")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().print(
                    "<html>\n" +
                            "<body>\n" +
                            "<h1>Hello Servlet!</h1>\n" +
                            "<p>This is my very first, embedded Tomcat, HTML Page!</p>\n" +
                            "</body>\n" +
                            "</html>");
        } else if (requestUri.equalsIgnoreCase("/invoices")) {
            List<Invoice> invoices = invoiceService.findAll();

            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(objectMapper.writeValueAsString(invoices));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String requestUri = request.getRequestURI();

        if (requestUri.equalsIgnoreCase("/invoices")) {
            String userId = request.getParameter("user_id");
            Integer amount = Integer.valueOf(request.getParameter("amount"));
            Invoice invoice = invoiceService.create(userId, amount);

            String json = objectMapper.writeValueAsString(invoice);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(json);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
