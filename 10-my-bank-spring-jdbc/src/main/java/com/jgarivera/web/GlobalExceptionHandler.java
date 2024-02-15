package com.jgarivera.web;

import com.jgarivera.dto.ErrorDto;
import com.jgarivera.dto.FieldErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<FieldErrorDto> fields = new ArrayList<>();

        for (FieldError fieldError : exception.getFieldErrors()) {
            fields.add(new FieldErrorDto(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return new ErrorDto("Method argument not valid", fields);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDto handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        return new ErrorDto("Required request body is missing");
    }
}
