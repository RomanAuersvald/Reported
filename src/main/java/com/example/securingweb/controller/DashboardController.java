package com.example.securingweb.controller;

import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.model.ReportedUser;
import com.example.securingweb.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private ProjectService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String setup(Model model) {
        ReportedUser user = getCurrentLoggedUser();
        Collection<Project> projectList = repository.findProjectsByOwnerId(user.getId());
        model.addAttribute("projects", projectList);
        model.addAttribute("user", user);
        return "/dashboard";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }


}
