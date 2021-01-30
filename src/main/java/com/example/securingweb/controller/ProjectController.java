package com.example.securingweb.controller;

import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class ProjectController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/project/all")
    public String showAllProject(Model model){
        msg = "Test response msg.";
        model.addAttribute("msg", msg);
        model.addAttribute("projects", repository.findAll());
        msg = "";
        return "project/allProjects";
    }

    @RequestMapping(value = "/project/addProject", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("project") Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "project/addProject";
        }
        repository.save(project);
        msg = "added";
        return "redirect:/project/all";
    }

    @GetMapping("/project/addTest")
    public String addTestProject(Model model){
        Project project = new Project(22, "Projekt 1", "projekt o projektu", "Johny");
        msg = "addtest project msg";
        model.addAttribute("msg", msg);
        model.addAttribute("project", project);
        msg = "";
        return "redirect:/project/all";

    }
}
