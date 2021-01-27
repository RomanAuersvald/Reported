package com.example.securingweb.dao;

import java.util.List;

import com.example.securingweb.model.ReportedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<ReportedUser, String> {

    public ReportedUser findByFirstName(String firstName);
    public List<ReportedUser> findByLastName(String lastName);

    // použito pro vyhledání uživatele podle uživ jména v přihlášení
    // ok then
    public ReportedUser findByUserName(String userName);
}