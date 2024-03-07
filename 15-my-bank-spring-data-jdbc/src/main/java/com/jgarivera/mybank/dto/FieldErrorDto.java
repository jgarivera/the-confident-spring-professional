package com.jgarivera.mybank.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FieldErrorDto {

    @JsonProperty("field")
    private String fieldName;

    @JsonProperty("message")
    private String errorMessage;

    public FieldErrorDto(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
