package com.HTT.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.HTT.backend.repositories.CompanyRepo;


@Controller
public class HomeController {

    @Autowired
    private CompanyRepo userRepository;

    @GetMapping("/health")
    public ResponseEntity<?> getMethodName() {
        userRepository.count();
        return new ResponseEntity("working fine",HttpStatus.OK);
    }
    
}
