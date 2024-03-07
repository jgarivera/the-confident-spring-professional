package com.jgarivera.mybank.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ErrorDto {

    private String message;

    @JsonProperty("field_errors")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldErrorDto> fieldErrors;

    public ErrorDto(String message) {
        this(message, null);
    }

    public ErrorDto(String message, List<FieldErrorDto> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorDto> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDto> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
