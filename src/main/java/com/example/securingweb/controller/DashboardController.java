package com.example.securingweb.controller;

import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.ProjectTaskRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.model.ProjectTask;
import com.example.securingweb.model.ReportedUser;
import com.example.securingweb.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private ProjectTaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String setup(Model model) {
        ReportedUser user = getCurrentLoggedUser();
        Collection<Project> projectList = repository.findProjectsByOwnerId(user.getId());
        model.addAttribute("projects", projectList);
        model.addAttribute("user", user);
        // seznam projektů a jejich progressu
        model.addAttribute("projectsProgress", getProjectsProgress(projectList));
        return "/dashboard";
    }

    // vrací seznam projektů a jejich progress v % na základě ukončených tasků
    private Map<Project, Double> getProjectsProgress(Collection<Project> projects){
        Map<Project, Double> progress = new HashMap<>();
        for (Project project: projects){
            Collection<ProjectTask> projectTasks = taskRepository.findProjectTasksByProjectId(project.getId());
            Double seconds = 0.0;
            for(ProjectTask task : projectTasks){
                if (task.taskComplete()){
                    seconds += task.returnTaskDuration();
                }
            }
            Double hours = seconds / 3600;
            Double percentDone = (hours /  project.getEstimatedHours()) * 100;
            progress.put(project, percentDone);
        }
        return progress;
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }


}
