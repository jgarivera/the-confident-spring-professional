package com.jgarivera.service;

import com.jgarivera.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TransactionService {

    private final List<Transaction> transactions;

    public TransactionService() {
        transactions = new CopyOnWriteArrayList<>();
    }

    public List<Transaction> findAll() {
        return transactions;
    }

    public Transaction create(BigDecimal amount, String reference) {
        Date timestamp = new Date(); // Now
        Transaction transaction = new Transaction(amount, reference, timestamp);
        transactions.add(transaction);

        return transaction;
    }
}
