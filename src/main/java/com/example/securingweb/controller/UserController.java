package com.example.securingweb.controller;

import com.example.securingweb.dao.AddressRepository;
import com.example.securingweb.dao.ClientRepository;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.Address;
import com.example.securingweb.model.Client;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    private String msg = "";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProjectRepository repository;


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/user/edit")
    public ModelAndView gibMeEditForm( Model model){
        ModelAndView form = new ModelAndView("user/edit");
        ReportedUser user = getCurrentLoggedUser();

        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/user/update/{id}", method = RequestMethod.POST)
    public String editUser(@PathVariable("id") String id, @Valid @ModelAttribute("user") ReportedUser user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            user.setId(id);
            return "user/edit";
        }
        userRepository.save(user);
        msg = "User s id: " + id + " byl uspesne editovan!";
        return "/dashboard";
    }

    @RequestMapping(value = "/user/addPremium/{id}")
    public String addPremiumToUser(@PathVariable String id){
        ReportedUser user = getCurrentLoggedUser();
        user.setRole("PREMIUM");
        userRepository.save(user);
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/user/deletePremium/{id}")
    public String deletePremiumUser(@PathVariable String id){
        ReportedUser user = getCurrentLoggedUser();
        user.setRole("USER");
        userRepository.save(user);
        return "redirect:/dashboard";
    }
}
