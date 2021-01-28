package com.example.securingweb.service;


import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportedUserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUnique(String login){
        boolean unique = true;

        if(userRepository.findByUsername(login) != null)
            unique = false;

        return unique;

    }
}
