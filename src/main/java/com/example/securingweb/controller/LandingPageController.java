package com.example.securingweb.controller;


import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
//@RequestMapping(path="/home")
public class LandingPageController {

    @Autowired
    private UserRepository userRepository;

//    @GetMapping(value = "/")
//    public String init(Model model){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        ReportedUser userDetails = ReportedUser.class.cast(principal);
//        String name = userDetails.getUsername();
//        model.addAttribute("name", name);
//        return "index";
//    }

    @GetMapping(value = "/")
    public String init(Model model){
        ReportedUser user = getCurrentLoggedUser();
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping(value="/registeruser")
    public String registerUser(Model model) {
        ReportedUser user = new ReportedUser();
        model.addAttribute("user", user);
        return "/registration";
    }

    @GetMapping(value="/loginuser")
    public String loginUser() {
        return "login";
    }

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == "anonymousUser" ){ return null;}
        ReportedUser userDetails = ReportedUser.class.cast(principal);

        return userRepository.findByUsername(userDetails.getUsername());
    }
}
