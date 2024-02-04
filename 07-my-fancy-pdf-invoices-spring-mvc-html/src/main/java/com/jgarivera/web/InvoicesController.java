package com.jgarivera.web;

import com.jgarivera.model.Invoice;
import com.jgarivera.service.InvoiceService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class InvoicesController {

    private final InvoiceService invoiceService;

    public InvoicesController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public List<Invoice> invoices() {
        // Spring does the conversion from Java objects to JSON for us!
        return invoiceService.findAll();
    }

    @PostMapping("/invoices")
    public Invoice createInvoice(
            @RequestParam("user_id") @NotBlank String userId,
            @RequestParam("amount") @Min(10) @Max(50) Integer amount) {
        return invoiceService.create(userId, amount);
    }
}
