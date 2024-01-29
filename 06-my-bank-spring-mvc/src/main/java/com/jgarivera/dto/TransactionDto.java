package com.jgarivera.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransactionDto {

    @NotNull
    @DecimalMin("0")
    private BigDecimal amount;

    @NotBlank
    private String reference;

    public TransactionDto() {
    }

    public TransactionDto(BigDecimal amount, String reference) {
        this.amount = amount;
        this.reference = reference;
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
}
