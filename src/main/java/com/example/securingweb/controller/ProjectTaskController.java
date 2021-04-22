package com.example.securingweb.controller;

        import com.example.securingweb.dao.LogRepository;
        import com.example.securingweb.dao.ProjectRepository;
        import com.example.securingweb.dao.ProjectTaskRepository;
        import com.example.securingweb.dao.UserRepository;
        import com.example.securingweb.model.*;
        import com.example.securingweb.service.ProjectTaskService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.validation.BindingResult;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.ModelAndView;

        import javax.validation.Valid;
        import java.time.LocalDateTime;
        import java.util.*;

@Controller
public class ProjectTaskController {
    private String msg = "";

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

    public ProjectTaskController(ProjectTaskRepository repository) {
        this.repository = repository;
    }

    // výpis tasks pro konkrétní projekt - id
    @GetMapping("/task/all/{id}")
    public String showAllTasksForProjectId(Model model, @PathVariable String id){
        model.addAttribute("msg", msg);
        model.addAttribute("tasks", repository.findProjectTasksByProjectId(id));
        model.addAttribute("projectId", id);
        model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        msg = "";
        return "task/all";
    }

    // výpis všech tasks, pokud není nastavený id, tak se nezobrazí tlačítko pro přidání tasku - není k čemu přiřadit
    @GetMapping("/task/all")
    public String showAllTasks(Model model){
//        msg = "Test response msg.";
        Collection<Project> projects = projectRepository.findProjectsByOwnerIdOrderByProjectStart(getCurrentLoggedUser().getId());
        Set<ProjectTask> tasks = new LinkedHashSet<>();
        for (Project p : projects){
            tasks.addAll(repository.findProjectTasksByProjectIdOrderByStartDateAsc(p.getId()));
        }

//        Collection<ProjectTask> tasks =  repository.findProjectTasksByProjectIdOrderByStartDateAsc(projectRepository.findProjectById(task.getP)); //.findAll());
        Map<ProjectTask, Project> tasksProject = new HashMap<>();
        for (ProjectTask task: tasks){
            String projId = task.getProjectId();
            Project project = projectRepository.findProjectById(projId);
            tasksProject.put(task, project);
        }

        model.addAttribute("msg", msg);
        model.addAttribute("tasks", tasksProject);
        model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        msg = "";
        return "task/all";
    }

    @GetMapping("/task/ad/{id}")
    public String bringMeAdd(Model model, @PathVariable String id){
        Optional<Project> p = projectRepository.findById(id);
        ProjectTask task = new ProjectTask();
        task.setProjectId(id);
        task.setHourRate(p.get().getHourRate()); // přiřazení výchozí hodinovky podle projektu
        task.setStartDate(LocalDateTime.now());
        model.addAttribute("task", task);
        model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "task/add"; // html
    }

    @RequestMapping(value = "/task/add", method = RequestMethod.POST)
    public String addTask(@Valid @ModelAttribute("task") ProjectTask task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "task/add";
        }
//        System.out.println(project);
        if (task.taskComplete()){
            Long duration = task.returnTaskDuration();
            System.out.println(duration);
        }
        repository.save(task);
        msg = "Task successfully added";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 1, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/task/all";
    }

    @RequestMapping(value = "/task/delete/{id}")
    public String deleteTask(@PathVariable String id) {
//        repository.deleteById(id);
        service.deleteTask(id);
        msg = "Task id: " + id + " successfully deleted";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 3, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/task/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/task/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("task/edit");
        ProjectTask task = service.grabTaskId(id);
        model.addAttribute("task", task);
        model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/task/update/{id}", method = RequestMethod.POST)
    public String editaTask(@PathVariable("id") String id, @Valid @ModelAttribute("task") ProjectTask task, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            task.setId(id);
            model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "task/edit";
        }
        service.saveTask(task);
        msg = "Task id: " + id + " successfully edited";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 2, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/task/all";
    }

    @RequestMapping(value = "/task/detail/{id}")
    public ModelAndView showMeDetail(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("task/detail");
        ProjectTask task = service.grabTaskId(id);
        model.addAttribute("task", task);
        model.addAttribute("user", getCurrentLoggedUser());
        model.addAttribute("projects", projectRepository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        return form;
    }

    @RequestMapping(value = "/task/completed/{id}")
    public String markTaskAsCompleted(@PathVariable String id){
        ProjectTask task = service.grabTaskId(id);
        task.setEndDate(LocalDateTime.now());
        service.saveTask(task);
        msg = "Task id: " + id + " marked as done";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 4, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/task/all";
    }
}
