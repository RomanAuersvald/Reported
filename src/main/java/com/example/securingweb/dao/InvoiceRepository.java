package com.example.securingweb.dao;

import com.example.securingweb.model.Invoice;
import com.example.securingweb.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, String> {
    Collection<Invoice> findAllByUserId(String id);
    Collection<Invoice> findAllByUserIdOrderByIdAsc(String id);
    Collection<Invoice> findAllByProjectId(String id);
    Collection<Invoice> findAllByProjectIdOrderByCreatedDesc(String id);
}
