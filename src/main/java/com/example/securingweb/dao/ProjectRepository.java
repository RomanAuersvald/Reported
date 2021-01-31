package com.example.securingweb.dao;
import com.example.securingweb.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {//MongoRepository<Project, String>{
}