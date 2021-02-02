package com.example.securingweb.dao;
import com.example.securingweb.model.Project;
import com.example.securingweb.model.ProjectTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {

    @Override
    long count();

    Collection<Project> findProjectsByOwnerId(String id);

    @Override
    Optional<Project> findById(String s);
}