package com.example.securingweb.controller;

//import com.example.securingweb.dao.CustomerRepository;
//import com.example.securingweb.model.Customer;
import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class CustomerRestController {
    @Autowired
    private UserRepository repository;

    @RequestMapping("customer/all")
    public List<ReportedUser> findAll(){
        final List<ReportedUser> customers = repository.findAll();
        System.out.println("Fetching customers from database {}");
        return customers;
    }

    @RequestMapping(value = "customer/" , method = RequestMethod.POST)
    public void save(@RequestBody ReportedUser customer){
        System.out.println("Storing customer in database {}");
        repository.save(customer);
    }
}
