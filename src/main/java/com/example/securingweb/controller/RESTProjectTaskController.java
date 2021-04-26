package com.example.securingweb.controller;


import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.ProjectTaskRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.*;
import com.example.securingweb.service.ProjectTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class RESTProjectTaskController {

    @Autowired
    private ProjectTaskRepository repository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTaskService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogRepository logRepository;


    @RequestMapping("task/startTaskEntry/{taskID}")
    public RestResponse startTaskEntry(@PathVariable String taskID) {
        RestResponse response = new RestResponse();
        ProjectTask task = service.grabTaskId(taskID);

        if (task != null){
            task.newTaskEntry(); // zahájení nové entry - no shit
            service.saveTask(task);
            response.setResponseStatus(RestResponse.OK);
        }else{
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        }

        return response;
    }

    @RequestMapping("task/endTaskEntry/{taskID}")
    public RestResponse endTaskEntry(@PathVariable String taskID) {
        RestResponse response = new RestResponse();
        ProjectTask task = service.grabTaskId(taskID);

        if (task != null){
            task.endLastTaskEntry(); // ukončení nové entry - no shit
            service.saveTask(task);
            response.setResponseStatus(RestResponse.OK);
        }else{
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        }

        return response;
    }

    @RequestMapping("task/getOngoingTaskEntry")
    public RestResponse getOngoingTaskEntry() {
        RestResponse response = new RestResponse();
        Project finalProject = null;
        ProjectTask finalTask = null;
        // toto je naprostý výpočetní masakr
        Collection<Project> projects = projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId());
        for (Project p: projects) {
            Collection<ProjectTask> projectTasks = repository.findProjectTasksByProjectId(p.getId());
            for (ProjectTask task: projectTasks){
                if (task.hasOngoingEntry()){
                    finalTask = task;
                    finalProject = p;
                }
                if (finalProject == null && finalTask == null){
                    continue;
                }else{
                    break;
                }
            }
            if (finalProject == null && finalTask == null){
                continue;
            }else{
                break;
            }
        }

        if (finalProject != null && finalTask != null){
            // něco běží, tak to vrátíme
            response.setResponseStatus(RestResponse.OK);
            response.setResponse(new ComplexResult(finalProject, finalTask));
        }else{
            response.setResponseStatus(RestResponse.NOT_FOUND);
            response.setResponse("");
        }

        return response;
    }

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }


}
