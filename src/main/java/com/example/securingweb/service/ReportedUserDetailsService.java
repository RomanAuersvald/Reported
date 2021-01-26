package com.example.securingweb.service;

import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReportedUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ReportedUser> optionalUser = Optional.ofNullable(userRepository.findByUserName(username));
        if(optionalUser.isPresent()) {
            return new ReportedUser( optionalUser.get().getUsername(), optionalUser.get().getPassword(), optionalUser.get().getRole(), optionalUser.get().getFirstName(), optionalUser.get().getLastName());
        }
        return null;
    }
}