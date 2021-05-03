package com.example.securingweb.service;

import com.example.securingweb.dao.LogRepository;
import com.example.securingweb.model.Log;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class LogService {

    @Autowired
    private EurekaClient eurekaClient;

    private final RestTemplate restTemplate;

    public LogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    private String requestURLForLoggingMethod(String method, String param){
        Application application = eurekaClient.getApplication("REPORTED-LOGGING-SERVICE");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        return "http://" + instanceInfo.getIPAddr() + ":" + instanceInfo.getPort() + "/logging" + ((method != "") ? ("/" + method) : "") + ((param != "") ? ("/" + param) : "");
    }

    public void logAction(Log log){
        String url = requestURLForLoggingMethod("", "");
        System.out.println("URL" + url);

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<Log> requestEntity = new HttpEntity<>(log, requestHeaders);
        ResponseEntity<Log> i = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Log>() {});

        if (i.getStatusCode() == HttpStatus.OK){
            System.out.println("Action logged.");
        }else {
            System.out.println("Failed to log.");
        }
    }
}
