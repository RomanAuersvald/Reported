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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ClientController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    public ClientController(ProjectRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/client/all")
    public String showAllProject(Model model){
//        msg = "Test response msg.";
        Collection<Client> clients = clientRepository.findClientByUserIdOrderByCompanyNameAsc(getCurrentLoggedUser().getId());
        model.addAttribute("clientAddress", getClientAddress(clients));
        model.addAttribute("msg", msg);
        model.addAttribute("clients", clients);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "client/all";
    }

    private Map<Client, Boolean> getClientAddress(Collection<Client> clients){
        Map<Client, Boolean> progress = new HashMap<>();
        for (Client client: clients){
            Address address = addressRepository.findAddressByOwnerId(client.getId());
            if (address != null){
                progress.put(client, Boolean.TRUE);
            }else{
                progress.put(client, Boolean.FALSE);
            }
        }
        return progress;
    }

    // most mezi allProjects.html -> add project (addProject.html) -> metoda project/add dole
    @GetMapping("/client/ad")
    public String bringMeAdd(Model model){
        ReportedUser user = getCurrentLoggedUser();
        String name = user.getNiceNameAndLastname();
        Client client = new Client();
        client.setUserId(user.getId());
        model.addAttribute("client", client);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "client/add";
    }

    @RequestMapping(value = "/client/add", method = RequestMethod.POST)
    public String addProject(@Valid @ModelAttribute("client") Client client, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "client/add";
        }
        clientRepository.save(client);
        msg = "Client byl uspesne pridan!";
        return "redirect:/client/all";
    }

    @RequestMapping(value = "/client/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        clientRepository.deleteById(id);
        msg = "Client s id: " + id + " byl uspesne odstranen!";
        return "redirect:/client/all";
    }


    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @RequestMapping(value = "/client/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("client/edit");
        Client client = clientRepository.findById(id).get();
        model.addAttribute("client", client);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/client/update/{id}", method = RequestMethod.POST)
    public String editProject(@PathVariable("id") String id, @Valid @ModelAttribute("client") Client client, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            client.setId(id);
            return "client/edit";
        }
        clientRepository.save(client);
        msg = "Client s id: " + id + " byl uspesne editovan!";
        return "redirect:/client/all";
    }
}
