package com.example.securingweb.controller;

import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.Log;
import com.example.securingweb.model.ReportedUser;
import com.example.securingweb.service.LogService;
import com.example.securingweb.service.ReportedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping(path="/registration")
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReportedUserService userService;

    private String user_message = "";

    @Autowired
    private LogService logService;

    @PostMapping(path="/addUser")
    public //@ResponseBody // - pouze pokud chceme výsledek ukázat
    String addNewUser (@Validated @ModelAttribute("user") ReportedUser user
            , BindingResult bindingResult, Model model) {

        if(!userService.isUnique(user.getUsername())){
            FieldError error = new FieldError("addUser", "login",
                    "Username already exists.");
            bindingResult.addError(error);
//            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("login", user.getUsername());
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            return "/user/registration";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setRole("USER");
        user.setPassword(encodedPassword);
        userRepository.save(user);
        logService.logAction(new Log("User has been created.", user.getId(), 1, LocalDateTime.now()));
        System.out.println("saved");
        user_message = "";
        return "redirect:/loginuser";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<ReportedUser> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
