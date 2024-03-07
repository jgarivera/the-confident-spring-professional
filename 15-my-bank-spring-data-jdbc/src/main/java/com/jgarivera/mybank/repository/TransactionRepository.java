package com.jgarivera.mybank.repository;

import com.jgarivera.mybank.model.Transaction;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends CrudRepository<Transaction, String> {

    @Query("SELECT * FROM \"transactions\" WHERE receiving_user_id = :userId")
    Iterable<Transaction> findAllByUserId(@Param("userId") String userId);
}
