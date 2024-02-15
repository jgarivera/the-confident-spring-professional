package com.jgarivera.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private String id;
    private BigDecimal amount;
    private String reference;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;
    private String slogan;
    private String receivingUserId;

    public Transaction(String id, BigDecimal amount, String reference, String receivingUserId, LocalDateTime timestamp, String slogan) {
        this.id = id;
        this.amount = amount;
        this.reference = reference;
        this.receivingUserId = receivingUserId;
        this.timestamp = timestamp;
        this.slogan = slogan;
    }

    public Transaction(BigDecimal amount, String reference, String receivingUserId, LocalDateTime timestamp, String slogan) {
        this(UUID.randomUUID().toString(), amount, reference, receivingUserId, timestamp, slogan);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getReceivingUserId() {
        return receivingUserId;
    }

    public void setReceivingUserId(String receivingUserId) {
        this.receivingUserId = receivingUserId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
