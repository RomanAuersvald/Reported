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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@Controller
public class ProjectController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private ProjectService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectTaskRepository taskRepository;

    public ProjectController(ProjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/project/all")
    public String showAllProject(Model model){
//        msg = "";
        model.addAttribute("msg", msg);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "project/all";
    }

    // most mezi allProjects.html -> add project (addProject.html) -> metoda project/add dole
    @GetMapping("/project/ad")
    public String bringMeAdd(Model model){

        ReportedUser user = getCurrentLoggedUser();
        String name = user.getNiceNameAndLastname();
        Project project = new Project("", "", user.getId(), LocalDateTime.now());
        model.addAttribute("project", project);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "project/add";
    }

    @RequestMapping(value = "/project/add", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("project") Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "project/add";
        }
        System.out.println(project);
        repository.save(project);
        msg = "Projekt byl uspesne pridan!";
        return "redirect:/project/all";
    }

    @GetMapping("/project/addTest")
    public String addTestProject(Model model){
        ReportedUser user = getCurrentLoggedUser();
        Project project = new Project( "Projekt 1", "projekt o projektu", user.getId(), LocalDateTime.now());
        msg = "Projeck byl uspesne pridan (Test)!";
        model.addAttribute("msg", msg);
        model.addAttribute("project", project);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        repository.save(project);
        return "redirect:/project/all";
    }

    @RequestMapping(value = "/project/delete/{id}")
    public String deleteProject(@PathVariable String id) {
//        repository.deleteById(id);
        service.deleteProject(id);
        msg = "Projekt s id: " + id + " byl uspesne odstranen!";
        return "redirect:/project/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/project/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("project/edit");
        Project project = service.grabProjectId(id);
        model.addAttribute("project", project);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/project/update/{id}", method = RequestMethod.POST)
    public String editProject(@PathVariable("id") String id, @Valid @ModelAttribute("project") Project project, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            project.setId(id);
            return "project/edit";
        }
        service.saveProject(project);
        msg = "Projekt s id: " + id + " byl uspesne editovan!";
        return "redirect:/project/all";
    }

    @GetMapping(value = "/project/detail/{id}")
    public ModelAndView showMeDetail(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("project/detail");
        Project project = service.grabProjectId(id);
        Collection<ProjectTask> tasks = taskRepository.findProjectTasksByProjectId(id);
        model.addAttribute("tasks", tasks);
        model.addAttribute("project", project);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }
}
