package com.jgarivera.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgarivera.context.MyFancyPdfInvoicesApplicationConfiguration;
import com.jgarivera.model.Invoice;
import com.jgarivera.service.InvoiceService;
import com.jgarivera.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.List;

public class MyFancyPdfInvoicesServlet extends HttpServlet {

    private UserService userService;
    private InvoiceService invoiceService;
    private ObjectMapper objectMapper;

    // Called whenever the servlet gets started
    @Override
    public void init() throws ServletException {
        // Create a Spring application context to start creating beans from our configuration class
        // If using XML configuration files, use `ClasspathXMLApplicationContext` instead
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                MyFancyPdfInvoicesApplicationConfiguration.class);

        // Add a shutdown hook to enable graceful shutdowns and trigger @PreDestroy methods in beans
        // This is automatically called for you in Spring Boot!
        context.registerShutdownHook();

        userService = context.getBean(UserService.class);
        invoiceService = context.getBean(InvoiceService.class);
        objectMapper = context.getBean(ObjectMapper.class);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String requestUri = request.getRequestURI();

        if (requestUri.equalsIgnoreCase("/")) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().print(
                    "<html>\n" +
                            "<body>\n" +
                            "<h1>Hello Servlet!</h1>\n" +
                            "<p>This is my very first, embedded Tomcat, HTML Page!</p>\n" +
                            "</body>\n" +
                            "</html>");
        } else if (requestUri.equalsIgnoreCase("/invoices")) {
            response.setContentType("application/json; charset=UTF-8");
            List<Invoice> invoices = invoiceService.findAll();
            response.getWriter().print(objectMapper.writeValueAsString(invoices));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String requestUri = request.getRequestURI();

        if (requestUri.equalsIgnoreCase("/invoices")) {
            String userId = request.getParameter("user_id");
            Integer amount = Integer.valueOf(request.getParameter("amount"));
            Invoice invoice = invoiceService.create(userId, amount);

            String json = objectMapper.writeValueAsString(invoice);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(json);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // The beans are made public so that they can be accessed from our test
    public UserService getUserService() {
        return userService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
