package com.example.securingweb.dao;

import com.example.securingweb.model.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface LogRepository extends CrudRepository<Log, String> {

    Collection<Log> findAllByUserIdOrderByIdDesc(String s);
}
