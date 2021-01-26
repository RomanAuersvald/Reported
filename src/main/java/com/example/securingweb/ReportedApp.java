package com.example.securingweb;

import com.example.securingweb.dao.UserRepository;
import com.example.securingweb.model.ReportedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class ReportedApp {

	public static void main(String[] args) throws Throwable {
		SpringApplication.run(ReportedApp.class, args);
	}

}
