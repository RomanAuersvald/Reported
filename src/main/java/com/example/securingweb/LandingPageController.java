package com.example.securingweb;


import com.example.securingweb.model.ReportedUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/home")
public class LandingPageController {


    @GetMapping(path="/registeruser")
    public String registerUser(Model model) {
        ReportedUser user = new ReportedUser();
        model.addAttribute("user", user);
        return "/registration";
    }

    @GetMapping(path="/loginuser")
    public String loginUser() {
        return "/login";
    }
}
