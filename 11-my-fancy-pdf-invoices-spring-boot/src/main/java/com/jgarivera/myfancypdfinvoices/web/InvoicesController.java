package com.jgarivera.myfancypdfinvoices.web;

import com.jgarivera.myfancypdfinvoices.dto.InvoiceDto;
import com.jgarivera.myfancypdfinvoices.model.Invoice;
import com.jgarivera.myfancypdfinvoices.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InvoicesController {

    private final InvoiceService invoiceService;

    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public List<Invoice> invoices() {
        return invoiceService.findAll();
    }

    @PostMapping("/invoices")
    public Invoice createInvoice(@Valid @RequestBody InvoiceDto dto) {
        return this.invoiceService.create(dto.getUserId(), dto.getAmount());
    }
}
