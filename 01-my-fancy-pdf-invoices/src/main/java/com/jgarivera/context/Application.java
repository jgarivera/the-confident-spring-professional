package com.jgarivera.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.service.InvoiceService;
import com.jgarivera.service.UserService;

// This class is a poor man's dependency injection container
public class Application {

    public static final ObjectMapper objectMapper = new ObjectMapper();
    public static final UserService userService = new UserService();
    public static final InvoiceService invoiceService = new InvoiceService(userService);
}
