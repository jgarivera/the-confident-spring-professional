package com.jgarivera.mybank.web;

import com.jgarivera.mybank.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WebsiteControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Test
    void it_shows_home_page() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    void it_shows_account_page_of_non_existing_user() throws Exception {
        String userId = "bob";

        mockMvc.perform(get("/account/{userId}", userId)
                        .header("Accept-Language", "en-US"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Account Transactions")))
                .andExpect(content().string(containsString(userId)))
                .andExpect(content().string(containsString("No transactions found")));
    }

    @Test
    void it_shows_transaction_in_account_page_of_existing_user() throws Exception {
        String reference = "mike", userId = "bob";
        BigDecimal amount = new BigDecimal("9999.75");
        String formattedAmount = NumberFormat.getCurrencyInstance(Locale.US).format(amount);

        transactionService.create(amount, reference, userId);

        mockMvc.perform(get("/account/{userId}", userId)
                        .header("Accept-Language", "en-US"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Account Transactions")))
                .andExpect(content().string(containsString(userId)))
                .andExpect(content().string(not(containsString("No transactions found"))))
                .andExpect(content().string(containsString(formattedAmount)))
                .andExpect(content().string(containsString(reference)));
    }

    @Test
    void it_shows_transactions_in_account_page_of_existing_user() throws Exception {
        String amount = "10.00", reference = "joshua", userId = "bob";

        transactionService.create(new BigDecimal(amount), reference, userId);

        mockMvc.perform(get("/account/{userId}", userId)
                        .header("Accept-Language", "en-US"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Account Transactions")))
                .andExpect(content().string(containsString(userId)))
                .andExpect(content().string(containsString(amount)))
                .andExpect(content().string(containsString(reference)));

        amount = "999.12";
        reference = "zane";
        userId = "bob";

        transactionService.create(new BigDecimal(amount), reference, userId);

        mockMvc.perform(get("/account/{userId}", userId)
                        .header("Accept-Language", "en-US"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Account Transactions")))
                .andExpect(content().string(containsString(userId)))
                .andExpect(content().string(containsString(amount)))
                .andExpect(content().string(containsString(reference)));
    }

    @Test
    void it_creates_transaction_in_account_page() throws Exception {
        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcus")
                        .param("amount", "777.22")
                        .param("reference", "jack"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/account/joey"));
    }

    @Test
    void it_validates_account_page_user_id_field() throws Exception {
        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "")
                        .param("amount", "100")
                        .param("reference", "jack"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "receivingUserId"));

        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcusmarcusmarcusmarcus")
                        .param("amount", "100")
                        .param("reference", "jack"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "receivingUserId"));
    }

    @Test
    void it_validates_account_page_amount_field() throws Exception {
        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcus")
                        .param("amount", "")
                        .param("reference", "jack"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "amount"));

        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcus")
                        .param("amount", "-1")
                        .param("reference", "jack"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "amount"));
    }

    @Test
    void it_validates_account_page_reference_field() throws Exception {
        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcus")
                        .param("amount", "100")
                        .param("reference", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "reference"));

        mockMvc.perform(post("/account/{userId}", "joey")
                        .header("Accept-Language", "en-US")
                        .param("receivingUserId", "marcus")
                        .param("amount", "100")
                        .param("reference", "jackjackjackjackjackjack"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("transactionForm", "reference"));
    }
}
