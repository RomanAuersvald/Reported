package com.example.securingweb.controller;

import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import com.example.securingweb.service.ReportedUserDetailsService;
import com.example.securingweb.service.ReportedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping(path="/addUser")
    public //@ResponseBody // - pouze pokud chceme výsledek ukázat
    String addNewUser (@Validated @ModelAttribute("user") ReportedUser user
            , BindingResult bindingResult, Model model) {

        if(!userService.isUnique(user.getUsername())){
            FieldError error = new FieldError("addUser", "username",
                    "Username already exists.");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            for (ObjectError error: bindingResult.getAllErrors()){
                System.out.println(error);
            }
            model.addAttribute("username", user.getUsername());
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastname", user.getLastName());
            return "/registration";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setRole("USER");
        user.setPassword(encodedPassword);
        userRepository.save(user);
        System.out.println("saved");
        user_message = "";
        return "redirect:/login";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<ReportedUser> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
