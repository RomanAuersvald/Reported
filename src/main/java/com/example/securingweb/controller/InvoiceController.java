package com.example.securingweb.controller;

import com.example.securingweb.dao.*;
import com.example.securingweb.model.*;
import com.example.securingweb.service.InvoiceService;
import com.example.securingweb.service.ProjectService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class InvoiceController {
    private String msg = "";

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    ProjectTaskRepository projectTaskRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    private LogRepository logRepository;


    @GetMapping("/invoice/all")
    public String showAllInvoices(Model model){
//        msg = "Test response msg.";
        Map<Invoice, Double> taskPrice = new HashMap<>();
        Double totalPrice = 0.0;
        Collection<Invoice> invoices = invoiceRepository.findAllByUserId(getCurrentLoggedUser().getId());
        for (Invoice invoice : invoices){
            Collection<ProjectTask> tasks = invoice.getTasks();
            for (ProjectTask task : tasks){
                Long duration = task.getTaskDuration();
                Double hourRate = task.getHourRate();
                Double price = hourRate * (duration / 3600);
                totalPrice += price;
            }
            taskPrice.put(invoice, totalPrice);
            totalPrice = 0.0;
        }
        model.addAttribute("price", taskPrice);
        model.addAttribute("msg", msg);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        model.addAttribute("invoices", invoiceRepository.findAllByUserIdOrderByIdAsc(getCurrentLoggedUser().getId()));
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
            if (task.taskComplete()){
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

    @RequestMapping(value = "/invoice/add", method = RequestMethod.POST)
    public String addInvoice(@Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<ProjectTask> closedTasks = new ArrayList<ProjectTask>();
            for (ProjectTask task : projectTaskRepository.findProjectTasksByProjectId(invoice.getProjectId())){
                if (task.taskComplete()){
                    closedTasks.add(task);
                }
            }
            ReportedUser user = getCurrentLoggedUser();
            model.addAttribute("projectTasks", closedTasks);
            model.addAttribute("clients", clientRepository.findClientsByUserId(user.getId()));
            model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
            model.addAttribute("user", getCurrentLoggedUser());
            return "invoice/add";
        }
        invoice.setClient(clientRepository.findById(invoice.getClientId()).get());
        for (String task : invoice.getTaskIds()){
            invoice.addTaskObjects(projectTaskRepository.findById(task).get());
        }
        invoice.setCreated(LocalDateTime.now());
        invoiceRepository.save(invoice);
        msg = "Invoice successfully created";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 1, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/invoice/all";
    }

    @RequestMapping(value = "/invoice/delete/{id}")
    public String deleteProject(@PathVariable String id) {
        invoiceRepository.deleteById(id);
        msg = "Invoice s id: " + id + " successfully deleted";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 3, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/invoice/all";
    }

    @RequestMapping(value = "/invoice/edit/{id}")
    public ModelAndView gibMeEditForm(@PathVariable String id, Model model){
        ModelAndView form = new ModelAndView("invoice/edit");
//        Project project = service.grabProjectId(id);
        Invoice invoice = invoiceRepository.findById(id).get();
        model.addAttribute("projectTasks", projectTaskRepository.findProjectTasksByProjectId(invoice.getProjectId()));
        model.addAttribute("clients", clientRepository.findClientsByUserId(getCurrentLoggedUser().getId()));
        model.addAttribute("projectName", repository.findProjectById(invoice.getProjectId()).getName());
        model.addAttribute("invoice", invoice);
        model.addAttribute("projects", repository.findProjectsByOwnerId(getCurrentLoggedUser().getId()));
        model.addAttribute("user", getCurrentLoggedUser());
        return form;
    }

    @RequestMapping(value = "/invoice/pdf/{id}", produces = "application/pdf")
    public ResponseEntity<byte[]> getMePDF(@PathVariable String id, Model model){
        Invoice invoice = invoiceRepository.findById(id).get();
        Address address = addressRepository.findAddressByOwnerId(invoice.getClientId());
        Address addressUser = addressRepository.findAddressByOwnerId(invoice.getUserId());
        String filledHTML = invoiceService.parseThymeleafTemplate(invoice, address, addressUser);

        String outputFolder = System.getProperty("user.home") + File.separator + id + ".pdf";
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputFolder);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(filledHTML);
        renderer.layout();
        try {
            renderer.createPDF(outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path path = Paths.get(outputFolder);
        byte[] pdfContents = null;
        try {
            pdfContents = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String filename = id + ".pdf";
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
                pdfContents, headers, HttpStatus.OK);
        return response;
    }

    @RequestMapping(value = "/invoice/update/{id}", method = RequestMethod.POST)
    public String editProject(@PathVariable("id") String id, @Valid @ModelAttribute("invoice") Invoice invoice, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            invoice.setId(id);
            return "invoice/edit";
        }
        invoiceRepository.save(invoice);
        msg = "Invoice s id: " + id + " successfully edited";
        Log notification = new Log(msg, getCurrentLoggedUser().getId(), 2, LocalDateTime.now());
        logRepository.save(notification);
        return "redirect:/invoice/all";
    }

    private ReportedUser getCurrentLoggedUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportedUser userDetails = ReportedUser.class.cast(principal);
        return userRepository.findByUsername(userDetails.getUsername());
    }
}
