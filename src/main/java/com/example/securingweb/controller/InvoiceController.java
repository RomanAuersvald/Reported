package com.example.securingweb.controller;

import com.example.securingweb.dao.*;
import com.example.securingweb.model.*;
import com.example.securingweb.service.InvoiceService;
import com.example.securingweb.service.LogService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class InvoiceController {
    private String msg = "";

    private final RestTemplate restTemplate;

    public InvoiceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ProjectTaskRepository projectTaskRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private LogService logService;

    private String requestURLForInvoiceMethod(String method, String param){
        Application application = eurekaClient.getApplication("REPORTED-INVOICE-SERVICE");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/invoices" + ((method != "") ? ("/" + method) : "") + ((param != "") ? ("/" + param) : "");
    }

    @GetMapping("/invoice/all")
    public String showAllInvoices(Model model){


       // HttpHeaders requestHeaders = new HttpHeaders();
       // requestHeaders.setContentType(MediaType.APPLICATION_JSON);
       // requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

       // // request body parameters
       // Map<String, Object> map = new HashMap<>();
       // map.put("username", "roman");
       // map.put("password", "roman");

       // HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(map, requestHeaders);
       // ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);


        Map<Invoice, Double> taskPrice = new HashMap<>();
        Double totalPrice = 0.0;
        String userID = getCurrentLoggedUser().getId();
        String url2 = requestURLForInvoiceMethod("all", userID);
        System.out.println("URL" + url2);
        List<Invoice> invoices = restTemplate.exchange(url2, HttpMethod.GET, null, new ParameterizedTypeReference<List<Invoice>>() {}).getBody();
        for (Invoice invoice : invoices){
            Collection<ProjectTask> tasks = invoice.getTasks();
            for (ProjectTask task : tasks){
                Long duration = task.returnTaskDuration();
                Double hourRate = task.getHourRate();
                Double price = hourRate * (duration / 3600);
                totalPrice += price;
            }
            taskPrice.put(invoice, totalPrice);
            totalPrice = 0.0;
        }
        model.addAttribute("price", taskPrice);
        model.addAttribute("msg", msg);
        fillModel(model);
        model.addAttribute("invoices", invoices);
        return "invoice/all";
    }

    // id - project id
    @GetMapping("/invoice/ad/{id}")
    public String bringMeAdd(Model model, @PathVariable String id){
        ReportedUser user = getCurrentLoggedUser();
        Project p = repository.findById(id).get();
        Invoice invoice = new Invoice();
        invoice.setProjectId(id);
        invoice.setProject(p);
        invoice.setCreated(LocalDateTime.now());
        invoice.setUser(user);
        invoice.setUserId(user.getId());
        List<ProjectTask> closedTasks = new ArrayList<ProjectTask>();
        for (ProjectTask task : projectTaskRepository.findProjectTasksByProjectId(id)){
            if (task.taskComplete() && (task.getHourRate() != null)){
                closedTasks.add(task);
            }
        }
        List<Client> clientsWithAddress = new ArrayList<Client>();
        for (Client client : clientRepository.findClientsByUserId(user.getId())){
            if (addressRepository.findAddressByOwnerId(client.getId()) != null){
                clientsWithAddress.add(client);
            }
        }
        model.addAttribute("projectName", p.getName());
        model.addAttribute("invoice", invoice);
        model.addAttribute("clients", clientsWithAddress);
        model.addAttribute("projectTasks", closedTasks);

        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return "invoice/add";
    }

    private String addInvoice(Optional<String> invoiceId, Invoice invoice, BindingResult bindingResult, Model model){
        Address address = addressRepository.findAddressByOwnerId(invoice.getClientId());
        Address addressUser = addressRepository.findAddressByOwnerId(invoice.getUserId());
        if (bindingResult.hasErrors() || (address == null) || (addressUser == null)) {
            if (invoiceId.isPresent()){
                invoice.setId(invoiceId.get());
            }
            injectModelWithClosedTasks(model, invoice);
            fillModel(model);
            return "invoice/add";
        }
        invoice.setClient(clientRepository.findById(invoice.getClientId()).get());
        invoice.setClientAddress(address);
        invoice.setUserAddress(addressUser);

        if (invoiceId.isPresent()){
            invoice.emptyProjectTasks();
            invoice.setId(invoiceId.get());
        }

        for (String task : invoice.getTaskIds()){
            invoice.addTaskObjects(projectTaskRepository.findById(task).get());
        }

        invoice.setCreated(LocalDateTime.now());

        String url = requestURLForInvoiceMethod("", "");
        System.out.println("URL post invoice " + url);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Invoice> requestEntity = new HttpEntity<>(invoice, requestHeaders);

        ResponseEntity<Invoice> i = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Invoice>() {});
        if (i.getStatusCode() == HttpStatus.OK){
            if (invoiceId.isPresent()){
                msg = "Invoice successfully updated";
            }else{
                msg = "Invoice successfully created";
            }
            logService.logAction(new Log(msg, getCurrentLoggedUser().getId(), 1, LocalDateTime.now()));
            return "redirect:/invoice/all";
        }else{
            injectModelWithClosedTasks(model, invoice);
            fillModel(model);
            return "invoice/add";
        }
    }

    @RequestMapping(value = "/invoice/add", method = RequestMethod.POST)
    public String newInvoice(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult, Model model) {
        return addInvoice(Optional.ofNullable(null), invoice, bindingResult, model);
    }

    @RequestMapping(value = "/invoice/update/{invoiceId}", method = RequestMethod.POST)
    public String updateInvoice(@PathVariable String invoiceId, @Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult, Model model) {
        return addInvoice(Optional.ofNullable(invoiceId), invoice, bindingResult, model);
    }

    private void injectModelWithClosedTasks(Model model, Invoice invoice){
        List<ProjectTask> closedTasks = new ArrayList<ProjectTask>();
        for (ProjectTask task : projectTaskRepository.findProjectTasksByProjectId(invoice.getProjectId())){
            if (task.taskComplete()){
                closedTasks.add(task);
            }
        }
        model.addAttribute("projectTasks", closedTasks);
    }

    @RequestMapping(value = "/invoice/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        String url = requestURLForInvoiceMethod("delete", id);
        System.out.println("URL invoice delete " + url);
        ResponseEntity response = restTemplate.getForEntity(url,ResponseEntity.class);
        if (response.getStatusCode() == HttpStatus.OK){
            msg = "Invoice s id: " + id + " successfully deleted";
            logService.logAction(new Log(msg, getCurrentLoggedUser().getId(), 3, LocalDateTime.now()));
        }else{
            msg = "Invoice s id: " + id + " failed to delete";
            logService.logAction(new Log(msg, getCurrentLoggedUser().getId(), 1, LocalDateTime.now()));
        }
        return "redirect:/invoice/all";
    }

    @RequestMapping(value = "/invoice/edit/{id}")
    public ModelAndView giveMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("invoice/edit");
        String url = requestURLForInvoiceMethod("", id);
        System.out.println("URL invoice edit " + url);
        Invoice invoice = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Invoice>() {}).getBody();
        model.addAttribute("projectTasks", projectTaskRepository.findProjectTasksByProjectId(invoice.getProjectId()));
        model.addAttribute("projectName", repository.findProjectById(invoice.getProjectId()).getName());
        model.addAttribute("invoice", invoice);
        fillModel(model);
        return form;
    }

    @RequestMapping(value = "/invoice/pdf/{id}", produces = "application/pdf")
    public ResponseEntity<byte[]> getMePDF(@PathVariable String id, Model model){
        String url = requestURLForInvoiceMethod("getPDF", id);
        System.out.println("URL invoice pdf" + url);
        return restTemplate.getForEntity(url,byte[].class);
    }

    private void fillModel(Model model){
        ReportedUser user = getCurrentLoggedUser();
        model.addAttribute("clients", clientRepository.findClientsByUserId(user.getId()));
        model.addAttribute("projects", repository.findProjectsByOwnerId(user.getId()));
        model.addAttribute("user", user);
    }

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }


}
