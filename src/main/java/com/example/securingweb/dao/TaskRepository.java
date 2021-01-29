package com.example.securingweb.dao;

import com.example.securingweb.model.ProjectTask;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<ProjectTask, String> {

    ProjectTask getTaskById(int Id);
}
