package com.example.securingweb.service;

import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Collection<Project> findAllProjects(){
        List<Project> projects = new ArrayList<>();
        for (Project project :projectRepository.findAll())
        {
            projects.add(project);
        }
        return projects;
    }

    public void saveProject(Project p) { projectRepository.save(p);}
}
