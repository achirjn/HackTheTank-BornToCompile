package com.HTT.backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/health")
    public ResponseEntity<?> getMethodName() {
        return new ResponseEntity("working fine",HttpStatus.OK);
    }
    @GetMapping("/loginsuccess")
    public ResponseEntity<?> getLoginSuccess() {
        return new ResponseEntity("you are logged in successfully",HttpStatus.OK);
    }
    
}
