package com.example.securingweb.controller;


import com.example.securingweb.model.ReportedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping(path="/home")
public class LandingPageController {

    @GetMapping(value = "/")
    public String init(Model model){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        String name = userDetails.getUsername();
        model.addAttribute("name", name);
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
}
