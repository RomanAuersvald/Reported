package com.example.securingweb.controller;


import com.example.securingweb.model.ReportedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @GetMapping(value = "/registerNewUser")
    public String registerNewUser(Model model) {
        ReportedUser user = new ReportedUser();
        model.addAttribute("user", user);
        return "/registration";
    }
}
