package com.example.securingweb.controller;

import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.model.Project;
import com.example.securingweb.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Controller
public class ProjectController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;
    @Autowired
    private ProjectService service;

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

    // most mezi allProjects.html -> add project (addProject.html) -> metoda project/add dole
    @GetMapping("/project/ad")
    public String bringMeAdd(Model model){
        Project project = new Project("project32", "project post test", "Johny");
        model.addAttribute("project", project);
        return "project/addProject";
    }

    @RequestMapping(value = "/project/add", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("project") Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "project/addProject";
        }
        System.out.println(project);
        repository.save(project);
        msg = "added";
        return "redirect:/project/all";
    }

    @GetMapping("/project/addTest")
    public String addTestProject(Model model){
        Project project = new Project( "Projekt 1", "projekt o projektu", "Johny");
        msg = "addtest project msg";
        model.addAttribute("msg", msg);
        model.addAttribute("project", project);
        repository.save(project);
        msg = "";
        return "redirect:/project/all";
    }

    @RequestMapping(value = "/project/delete/{id}")
    public String deleteProject(@PathVariable String id) {
//        repository.deleteById(id);
        service.deleteProject(id);
        return "redirect:/project/all";
    }
}
