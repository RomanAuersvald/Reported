package com.example.securingweb.controller;

import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogController {
    private String msg = "";

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @GetMapping("/log")
    public String showLog(Model model){
        model.addAttribute("logs", logRepository.findAllByUserIdOrderByIdDesc(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        model.addAttribute("projects", projectRepository.findProjectsByOwnerIdOrderByIdAsc(getCurrentLoggedUser().getId()));
        return "user/log";
    }
}
