package com.jgarivera.service;

import com.jgarivera.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final List<Transaction> transactions;
    private final String slogan;

    public TransactionService(@Value("${bank.slogan}") String slogan) {
        transactions = new CopyOnWriteArrayList<>();
        this.slogan = slogan;
    }

    public List<Transaction> findAll() {
        return transactions;
    }

    public List<Transaction> findAllFromUser(String userId) {
        return transactions.stream()
                .filter(transaction -> transaction.getReceivingUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public Transaction create(BigDecimal amount, String reference, String receivingUserId) {
        Date timestamp = new Date(); // Now
        Transaction transaction = new Transaction(amount, reference, receivingUserId, timestamp, slogan);
        transactions.add(transaction);

        return transaction;
    }

    public String getSlogan() {
        return slogan;
    }
}
