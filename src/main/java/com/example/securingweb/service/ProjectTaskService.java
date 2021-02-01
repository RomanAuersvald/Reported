package com.example.securingweb.service;

import com.example.securingweb.dao.ProjectTaskRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.model.ProjectTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectTaskService {
    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    public Collection<ProjectTask> findAllTasks(){
        List<ProjectTask> projects = new ArrayList<>();
        for (ProjectTask project :projectTaskRepository.findAll())
        {
            projects.add(project);
        }
        return projects;
    }

    public void saveTask(ProjectTask p) { projectTaskRepository.save(p);}

    public void deleteTask(String id) {
        //Optional<Project> project = projectRepository.findById(id);
        //if (project.isPresent()){
        //    projectRepository.delete(project.get());
        //}
        projectTaskRepository.deleteById(id);
    }

    public ProjectTask grabProjectId(String id) {
        return projectTaskRepository.findById(id).get();
    }
}
