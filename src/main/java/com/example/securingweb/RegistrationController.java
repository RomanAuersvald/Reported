package com.example.securingweb;

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
    String addNewUser (@RequestParam String firstName
            , @RequestParam String lastName
            , @RequestParam String username
            , @RequestParam String password
            ,@Valid @ModelAttribute("user") ReportedUser user
            , BindingResult bindingResult, Model model) {

        if(!userService.isUnique(username)){
            FieldError error = new FieldError("addDomain", "username",
                    "Username already exists.");
            bindingResult.addError(error);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("username", username);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastname", lastName);
            return "/registration";
        }

        String encodedPassword = passwordEncoder.encode(password);

        ReportedUser n = new ReportedUser(
                username,
                encodedPassword,
                "USER",
                firstName,
                lastName
        );
        userRepository.save(n);
        System.out.println("saved");
        user_message = "Nová doména byla úspěšně přidána";
        return "redirect:/login";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<ReportedUser> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }
}
