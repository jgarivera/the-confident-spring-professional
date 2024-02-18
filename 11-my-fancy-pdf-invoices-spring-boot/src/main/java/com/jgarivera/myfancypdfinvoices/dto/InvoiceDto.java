package com.jgarivera.myfancypdfinvoices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

// Data transfer object
public class InvoiceDto {

    @JsonProperty("user_id")
    @NotBlank
    private String userId;

    @Min(10)
    @Max(50)
    private Integer amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
