package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.model.Transaction;
import com.jgarivera.service.TransactionService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class MyBankServlet extends HttpServlet {

    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;

    private final static String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";

    public MyBankServlet(TransactionService transactionService, ObjectMapper objectMapper) {
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        if (uri.equalsIgnoreCase("/transactions")) {
            List<Transaction> transactions = transactionService.findAll();
            String json = objectMapper.writeValueAsString(transactions);

            response.setContentType(JSON_CONTENT_TYPE);
            response.getWriter().print(json);
        } else {
            response.getWriter().print("Welcome to My Bank!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        if (uri.equalsIgnoreCase("/transactions")) {
            BigDecimal amount = new BigDecimal(request.getParameter("amount"))
                    .setScale(2, RoundingMode.UP);
            String reference = request.getParameter("reference");

            Transaction transaction = transactionService.create(amount, reference);
            String json = objectMapper.writeValueAsString(transaction);

            response.setContentType(JSON_CONTENT_TYPE);
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().print(json);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
