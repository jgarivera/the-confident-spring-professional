package com.jgarivera.service;

import com.jgarivera.model.Invoice;
import com.jgarivera.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InvoiceService {

    // Store invoices in a thread-safe list
    private final List<Invoice> invoices = new CopyOnWriteArrayList<>();

    private final UserService userService;
    private final String cdnUrl;

    // Favor using constructor injection for specifying mandatory dependencies because:
    // - Field injection hides dependencies and makes the code harder to test
    // - Constructor injection allows this class to be usable outside a Spring IoC container

    // Use setter injection for specifying optional dependencies
    public InvoiceService(UserService userService, @Value("${cdn.url}") String cdnUrl) {
        this.userService = userService;
        this.cdnUrl = cdnUrl;
    }

    public List<Invoice> findAll() {
        return invoices;
    }

    public Invoice create(String userId, Integer amount) {
        User user = userService.findById(userId);

        if (user == null) {
            throw new IllegalStateException();
        }

        // TODO: real pdf creation and storing it on network server
        Invoice invoice = new Invoice(userId, amount, cdnUrl + "/images/default/sample.pdf");
        invoices.add(invoice);

        return invoice;
    }

    @PostConstruct
    public void init() {
        System.out.println("Fetching PDF Template from S3...");
        // TODO: download from S3 and save locally
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("Deleting downloaded templates...");
        // TODO: actual deletion of PDFs
    }
}
