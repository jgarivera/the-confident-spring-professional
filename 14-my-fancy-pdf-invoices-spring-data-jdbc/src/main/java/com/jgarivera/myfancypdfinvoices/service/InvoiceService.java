package com.jgarivera.myfancypdfinvoices.service;

import com.jgarivera.myfancypdfinvoices.model.Invoice;
import com.jgarivera.myfancypdfinvoices.repository.InvoiceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InvoiceService {

    // Spring Data JDBC will provide our repository
    private final InvoiceRepository invoiceRepository;
    private final String cdnUrl;

    // Favor using constructor injection for specifying mandatory dependencies because:
    // - Field injection hides dependencies and makes the code harder to test
    // - Constructor injection allows this class to be usable outside a Spring IoC container

    // Use setter injection for specifying optional dependencies
    public InvoiceService(InvoiceRepository invoiceRepository, @Value("${cdn.url}") String cdnUrl) {
        this.invoiceRepository = invoiceRepository;
        this.cdnUrl = cdnUrl;
    }

    @Transactional
    public Iterable<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    @Transactional
    public Iterable<Invoice> findByUserId(String userId) {
        return invoiceRepository.findByUserId(userId);
    }

    @Transactional
    public Invoice create(String userId, Integer amount) {
        String pdfUrl = cdnUrl + "/images/default/sample.pdf";

        var invoice = new Invoice();
        invoice.setPdfUrl(pdfUrl);
        invoice.setAmount(amount);
        invoice.setUserId(userId);

        // Repository save will set the generated ID on the invoice for us
        return invoiceRepository.save(invoice);
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
