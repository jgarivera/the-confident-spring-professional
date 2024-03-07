package com.jgarivera.mybank.service;

import com.jgarivera.mybank.model.Transaction;
import com.jgarivera.mybank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final String slogan;

    public TransactionService(TransactionRepository transactionRepository, @Value("${bank.slogan}") String slogan) {
        this.transactionRepository = transactionRepository;
        this.slogan = slogan;
    }

    @Transactional
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Transactional
    public Iterable<Transaction> findAllFromUser(String userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Transactional
    public Transaction create(BigDecimal amount, String reference, String receivingUserId) {
        LocalDateTime timestampNow = LocalDateTime.now();

        var transaction = new Transaction(amount, reference, receivingUserId, timestampNow, slogan);
        return transactionRepository.save(transaction);
    }

    public String getSlogan() {
        return slogan;
    }
}
