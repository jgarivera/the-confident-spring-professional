package com.jgarivera.myfancypdfinvoices.web;

import com.jgarivera.myfancypdfinvoices.dto.InvoiceDto;
import com.jgarivera.myfancypdfinvoices.model.Invoice;
import com.jgarivera.myfancypdfinvoices.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicesController {

    private final InvoiceService invoiceService;

    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public Iterable<Invoice> invoices() {
        return invoiceService.findAll();
    }

    @GetMapping("/invoices/{userId}")
    public Iterable<Invoice> invoicesByUserId(@PathVariable("userId") String userId) {
        return invoiceService.findByUserId(userId);
    }

    @PostMapping("/invoices")
    public Invoice createInvoice(@Valid @RequestBody InvoiceDto dto) {
        return this.invoiceService.create(dto.getUserId(), dto.getAmount());
    }
}
