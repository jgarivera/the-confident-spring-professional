package com.jgarivera.myfancypdfinvoices.repository;

import com.jgarivera.myfancypdfinvoices.model.Invoice;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InvoiceRepository extends CrudRepository<Invoice, String> {

    @Query("SELECT id, pdf_url, user_id, amount FROM \"invoices\" WHERE user_id = :userId")
    Iterable<Invoice> findByUserId(@Param("userId") String userId);

}
