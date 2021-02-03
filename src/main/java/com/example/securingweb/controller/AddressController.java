package com.example.securingweb.controller;

import com.example.securingweb.dao.*;
import com.example.securingweb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AddressController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ClientRepository clientRepository;

    public AddressController(ProjectRepository repository) {
        this.repository = repository;
    }

    @Autowired
    private LogRepository logRepository;

    @GetMapping("/address/ad")
    public String bringMeAdd(Model model){
        ReportedUser user = getCurrentLoggedUser();
        String name = user.getNiceNameAndLastname();
        Address address = new Address();
        model.addAttribute("address", address);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "address/add";
    }

    @GetMapping("/address/ad/{id}")
    public String bringMeAddId(Model model, @PathVariable String id){
        Address ad = new Address();
        ad.setOwnerId(id);
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()){
            //client
            model.addAttribute("client", client.get());
        }

        model.addAttribute("address", ad);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "address/add"; // html
    }

    @RequestMapping(value = "/address/add", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("address") Address address, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("client", clientRepository.findById(address.getOwnerId()).get());
            model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "address/add";
        }
        addressRepository.save(address);
        msg = "Adress successfully added";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 1, LocalDateTime.now());
        logRepository.save(notification);
        Optional<Client> client = clientRepository.findById(address.getOwnerId());
        if (client.isPresent()){
            //client
            return "redirect:/client/all";
        }else{
            return "redirect:/user/detail";
        }
    }

    @RequestMapping(value = "/address/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        addressRepository.deleteById(id);
        msg = "Adress id: " + id + "  successfully deleted";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 3, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/client/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/address/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("address/edit");
        Address address = addressRepository.findAddressByOwnerId(id);
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()){
            //client
            model.addAttribute("client", client.get());
        }
        model.addAttribute("address", address);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/address/update/{id}", method = RequestMethod.POST)
    public String editProject(@PathVariable("id") String id, @Valid @ModelAttribute("address") Address address, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            address.setId(id);
            return "address/edit";
        }
        addressRepository.save(address);
        msg = "Adress s id: " + id + " successfully updated";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 2, LocalDateTime.now());
        logRepository.save(notification);
        Optional<Client> client = clientRepository.findById(address.getOwnerId());
        if (client.isPresent()){
            //client
            return "redirect:/client/all";
        }else{
            return "redirect:/user/detail";
        }

    }
}
