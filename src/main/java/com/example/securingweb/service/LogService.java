package com.example.securingweb.service;

import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.model.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private LogRepository logRepository;

    public void saveToLog(Log l){
        logRepository.save(l);
    }
}
