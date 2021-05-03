package com.example.securingweb.controller;

import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.Invoice;
import com.example.securingweb.model.Log;
import com.example.securingweb.model.ReportedUser;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class LogController {
    private String msg = "";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EurekaClient eurekaClient;

    private final RestTemplate restTemplate;

    public LogController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    private String requestURLForLoggingMethod(String method, String param){
        Application application = eurekaClient.getApplication("REPORTED-LOGGING-SERVICE");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/logging" + ((method != "") ? ("/" + method) : "") + ((param != "") ? ("/" + param) : "");
    }

    @GetMapping("/log")
    public String showLog(Model model){
        ReportedUser user = getCurrentLoggedUser();
        String url = requestURLForLoggingMethod("", user.getId());
        System.out.println("URL" + url);
        List<Log> logs = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Log>>() {}).getBody();
        model.addAttribute("logs", logs);
        model.addAttribute("user", user);
        model.addAttribute("projects", projectRepository.findProjectsByOwnerIdOrderByIdAsc(getCurrentLoggedUser().getId()));
        return "user/log";
    }
}
