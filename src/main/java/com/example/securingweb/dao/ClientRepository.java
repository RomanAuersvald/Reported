package com.example.securingweb.dao;

import com.example.securingweb.model.Client;
import com.example.securingweb.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {

    Collection<Client> findClientsByUserId(String id);
    Collection<Client> findClientByUserIdOrderByCompanyNameAsc(String id);
}
