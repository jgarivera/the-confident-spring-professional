package com.jgarivera.service;

import com.jgarivera.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    private final JdbcTemplate jdbcTemplate;
    private final String slogan;

    public TransactionService(JdbcTemplate jdbcTemplate, @Value("${bank.slogan}") String slogan) {
        this.jdbcTemplate = jdbcTemplate;
        this.slogan = slogan;
    }

    @Transactional
    public List<Transaction> findAll() {
        return jdbcTemplate.query("SELECT * FROM transactions",
                (resultSet, rowNum) -> createFromResultSet(resultSet));
    }

    @Transactional
    public List<Transaction> findAllFromUser(String userId) {
        return jdbcTemplate.query("SELECT * FROM transactions WHERE receiving_user_id = ?",
                (resultSet, rowNum) -> createFromResultSet(resultSet), userId);
    }

    private Transaction createFromResultSet(ResultSet resultSet) throws SQLException {
        String id = resultSet.getObject("id", UUID.class).toString();
        BigDecimal amount = resultSet.getBigDecimal("amount");
        String reference = resultSet.getString("reference");
        String receivingUserId = resultSet.getString("receiving_user_id");
        LocalDateTime timestamp = resultSet.getTimestamp("timestamp").toLocalDateTime();

        return new Transaction(id, amount, reference, receivingUserId, timestamp, slogan);
    }

    @Transactional
    public Transaction create(BigDecimal amount, String reference, String receivingUserId) {
        LocalDateTime timestampNow = LocalDateTime.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO transactions (amount, reference, timestamp, receiving_user_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, amount);
            ps.setString(2, reference);
            ps.setTimestamp(3, Timestamp.valueOf(timestampNow));
            ps.setString(4, receivingUserId);

            return ps;
        }, keyHolder);

        String uuid = retrieveUuidFromKeyHolder(keyHolder);

        return new Transaction(uuid, amount, reference, receivingUserId, timestampNow, slogan);
    }

    private String retrieveUuidFromKeyHolder(KeyHolder keyHolder) {
        Map<String, Object> keys = keyHolder.getKeys();
        boolean hasKeys = keys != null && !keys.isEmpty();

        String uuid = null;

        if (hasKeys) {
            uuid = keys.values().iterator().next().toString();
        }

        return uuid;
    }

    public String getSlogan() {
        return slogan;
    }
}
