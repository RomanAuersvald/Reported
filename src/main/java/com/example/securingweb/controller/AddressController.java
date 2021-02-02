package com.example.securingweb.controller;

import com.example.securingweb.dao.AddressRepository;
import com.example.securingweb.dao.ClientRepository;
import com.example.securingweb.dao.ProjectRepository;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

//    @GetMapping("/address/all")
//    public String showAllProject(Model model){
////        msg = "Test response msg.";
//        model.addAttribute("msg", msg);
//        model.addAttribute("addresses", addressRepository.findAddressByOwnerId(getCurrentLoggedUser().getId()));
//        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
//        model.addAttribute("user", getCurrentLoggedUser());
//        return "address/allAddress";
//    }

    // most mezi allProjects.html -> add project (addProject.html) -> metoda project/add dole
    @GetMapping("/address/ad")
    public String bringMeAdd(Model model){
        ReportedUser user = getCurrentLoggedUser();
        String name = user.getNiceNameAndLastname();
        Address address = new Address();
        model.addAttribute("address", address);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "add";
    }

    @GetMapping("/address/ad/{id}")
    public String bringMeAdd(Model model, @PathVariable String id){
        Address ad = new Address();
        ad.setOwnerId(id);
        Client client = clientRepository.findById(id).get();
        model.addAttribute("client", client);
        model.addAttribute("address", ad);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "add"; // html
    }

    @RequestMapping(value = "/address/add", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("address") Address address, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "add";
        }
        addressRepository.save(address);
        msg = "Adresa byl uspesne pridan!";
        return "redirect:/client/all";
    }

    @RequestMapping(value = "/address/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        addressRepository.deleteById(id);
        msg = "Adresa s id: " + id + " byl uspesne odstranen!";
        return "redirect:/client/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/address/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("edit");
        Address address = addressRepository.findAddressByOwnerId(id);
        Client client = clientRepository.findById(id).get();
        model.addAttribute("client", client);
        model.addAttribute("address", address);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/address/update/{id}", method = RequestMethod.POST)
    public String editProject(@PathVariable("id") String id, @Valid @ModelAttribute("address") Address address, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            address.setId(id);
            return "edit";
        }
        addressRepository.save(address);
        msg = "Adresa s id: " + id + " byl uspesne editovan!";
        return "redirect:/client/all";
    }
}
