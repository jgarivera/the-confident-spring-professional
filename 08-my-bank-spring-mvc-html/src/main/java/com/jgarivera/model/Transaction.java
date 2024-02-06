package com.jgarivera.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Transaction {

    private String id;
    private BigDecimal amount;
    private String reference;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestamp;
    private String slogan;
    private String receivingUserId;

    public Transaction(BigDecimal amount, String reference, String receivingUserId, Date timestamp, String slogan) {
        this.id = UUID.randomUUID().toString();
        this.amount = amount;
        this.reference = reference;
        this.receivingUserId = receivingUserId;
        this.timestamp = timestamp;
        this.slogan = slogan;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}
