package com.jgarivera.web;

import com.jgarivera.dto.TransactionDto;
import com.jgarivera.model.Transaction;
import com.jgarivera.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionController {

    public TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions() {
        return transactionService.findAll();
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody @Valid TransactionDto dto) {
        return transactionService.create(dto.getAmount(), dto.getReference());
    }
}
