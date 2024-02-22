package com.jgarivera.mybank.web;

import com.jgarivera.mybank.service.TransactionService;
import com.jgarivera.mybank.web.forms.TransactionForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Controller
public class WebsiteController {

    private final TransactionService transactionService;

    public WebsiteController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("slogan", transactionService.getSlogan());
        return "index.html";
    }

    @GetMapping("/account/{userId}")
    public String account(Model model, @PathVariable("userId") String userId) {
        model.addAttribute("userId", userId);
        model.addAttribute("transactionForm", new TransactionForm());
        model.addAttribute("transactions", transactionService.findAllFromUser(userId));
        return "account.html";
    }

    @PostMapping("/account/{userId}")
    public String createTransaction(
            @ModelAttribute @Valid TransactionForm transactionForm,
            BindingResult bindingResult,
            @PathVariable("userId") String userId,
            Model model
    ) {
        model.addAttribute("userId", userId);
        model.addAttribute("transactions", transactionService.findAllFromUser(userId));

        if (bindingResult.hasErrors()) {
            return "account.html";
        }

        BigDecimal amount = transactionForm.getAmount();
        String reference = transactionForm.getReference();
        String receivingUserId = transactionForm.getReceivingUserId();

        transactionService.create(amount, reference, receivingUserId);

        model.addAttribute("successfulTransaction", "true");

        return "redirect:/account/" + userId;
    }
}
