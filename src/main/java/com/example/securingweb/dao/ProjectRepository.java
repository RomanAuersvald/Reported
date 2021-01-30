package com.example.securingweb.dao;
import com.example.securingweb.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String>{


    List<Project> getProjectsByOwner(String owner);

    Project getProjectById(int Id);

}
