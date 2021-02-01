package com.example.securingweb.controller;

        import com.example.securingweb.dao.ProjectRepository;
        import com.example.securingweb.dao.ProjectTaskRepository;
        import com.example.securingweb.dao.UserRepository;
        import com.example.securingweb.model.Project;
        import com.example.securingweb.model.ProjectTask;
        import com.example.securingweb.model.ReportedUser;
        import com.example.securingweb.service.ProjectService;
        import com.example.securingweb.service.ProjectTaskService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.BindingResult;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.ModelAndView;

        import javax.validation.Valid;
        import java.util.Collection;

@Controller
public class ProjectTaskController {
    private String msg = "";

    @Autowired
    private ProjectTaskRepository repository;

    @Autowired
    private ProjectTaskService service;

    @Autowired
    private UserRepository userRepository;

    public ProjectTaskController(ProjectTaskRepository repository) {
        this.repository = repository;
    }

    // výpis tasks pro konkrétní projekt - id
    @GetMapping("/tasks/all/{id}")
    public String showAllTasksForProjectId(Model model, @PathVariable String id){
        msg = "Test response msg.";
        model.addAttribute("msg", msg);
        model.addAttribute("tasks", repository.findProjectTasksByProjectId(id));
        model.addAttribute("projectId", id);
        msg = "";
        return "task/allTasks";
    }

    // výpis všech tasks, pokud není nastavený id, tak se nezobrazí tlačítko pro přidání tasku - není k čemu přiřadit
    @GetMapping("/tasks/all")
    public String showAllTasks(Model model){
        msg = "Test response msg.";
        model.addAttribute("msg", msg);
        model.addAttribute("tasks", repository.findAll());
        msg = "";
        return "task/allTasks";
    }

    @GetMapping("/tasks/ad/{id}")
    public String bringMeAdd(Model model, @PathVariable String id){
        ProjectTask task = new ProjectTask();
        task.setProjectId(id);
        model.addAttribute("task", task);

        return "task/addTask"; // html
    }

    @RequestMapping(value = "/tasks/add", method = RequestMethod.POST)
    public String addTask(@Valid @ModelAttribute("task") ProjectTask task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "task/addTask";
        }
//        System.out.println(project);
        repository.save(task);
        msg = "added";
        return "redirect:/tasks/all";
    }

    @RequestMapping(value = "/tasks/delete/{id}")
    public String deleteTask(@PathVariable String id) {
//        repository.deleteById(id);
        service.deleteTask(id);
        return "redirect:/tasks/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/tasks/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("task/editTask");
        ProjectTask task = service.grabProjectId(id);
        model.addAttribute("task", task);
        return form;
    }

    @RequestMapping(value = "/tasks/update/{id}", method = RequestMethod.POST)
    public String editaTask(@PathVariable("id") String id, @Valid @ModelAttribute("task") ProjectTask task, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            task.setId(id);
            return "task/editTask";
        }
        service.saveTask(task);
        return "redirect:/tasks/all";
    }
}
