package com.jgarivera.web;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// This will write back error information in JSON or XML to clients
// whenever a controller, @RestController and @Controller encounters
// a handled exception. @ControllerAdvice returns HTML instead
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        // TODO: You can return a custom object that will get converted to JSON or XML
        return "Sorry, that was not quite right: " + exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintValidation(ConstraintViolationException exception) {
        return "Sorry, that was not quite right: " + exception.getMessage();
    }
}
