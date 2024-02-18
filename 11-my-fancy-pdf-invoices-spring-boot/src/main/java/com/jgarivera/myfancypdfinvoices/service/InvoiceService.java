package com.jgarivera.myfancypdfinvoices.service;

import com.jgarivera.myfancypdfinvoices.model.Invoice;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive;

@Component
public class InvoiceService {

    // Spring Boot Starter JDBC will autoconfigure and provide this JDBC template for us
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;
    private final String cdnUrl;

    // Favor using constructor injection for specifying mandatory dependencies because:
    // - Field injection hides dependencies and makes the code harder to test
    // - Constructor injection allows this class to be usable outside a Spring IoC container

    // Use setter injection for specifying optional dependencies
    public InvoiceService(UserService userService, JdbcTemplate jdbcTemplate, @Value("${cdn.url}") String cdnUrl) {
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
        this.cdnUrl = cdnUrl;
    }

    @Transactional
    public List<Invoice> findAll() {
        System.out.println("Is a database transaction open? = " + isActualTransactionActive());
        return jdbcTemplate.query("SELECT id, user_id, pdf_url, amount FROM invoices", (resultSet, rowNum) -> {
            var invoice = new Invoice();
            invoice.setId(resultSet.getObject("id", UUID.class).toString());
            invoice.setPdfUrl(resultSet.getString("pdf_url"));
            invoice.setUserId(resultSet.getString("user_id"));
            invoice.setAmount(resultSet.getInt("amount"));

            return invoice;
        });
    }

    @Transactional
    public Invoice create(String userId, Integer amount) {
        System.out.println("Is a database transaction open? = " + isActualTransactionActive());

        String pdfUrl = cdnUrl + "/images/default/sample.pdf";

        // JDBC will make sure that the generated ID will available in this object
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO invoices (user_id, pdf_url, amount) VALUES (?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, userId);
            ps.setString(2, pdfUrl);
            ps.setInt(3, amount);

            return ps;
        }, keyHolder);

        String uuid = retrieveUuidFromKeyHolder(keyHolder);

        Invoice invoice = new Invoice();
        invoice.setId(uuid);
        invoice.setPdfUrl(pdfUrl);
        invoice.setAmount(amount);
        invoice.setUserId(userId);

        return invoice;
    }

    // This is an ugly way to return the auto-generated UUID primary key from the database
    private String retrieveUuidFromKeyHolder(KeyHolder keyHolder) {
        Map<String, Object> keys = keyHolder.getKeys();
        boolean hasKeys = keys != null && !keys.isEmpty();

        String uuid = null;

        if (hasKeys) {
            uuid = keys.values().iterator().next().toString();
        }

        return uuid;
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
