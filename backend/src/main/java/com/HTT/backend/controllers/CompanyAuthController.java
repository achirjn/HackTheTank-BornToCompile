package com.HTT.backend.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HTT.backend.dto.CompanyDto;
import com.HTT.backend.entities.Company;
import com.HTT.backend.helper.RandomGenerator;
import com.HTT.backend.services.CompanyService;
import com.HTT.backend.services.impl.EmailSender;

@RestController
@RequestMapping("/auth")
public class CompanyAuthController {

    private PasswordEncoder passwordEncoder;
    private CompanyService companyService;
    private EmailSender emailSender;

    @Value("${frontend.base.url}")
    private String frontendUrl;

    public CompanyAuthController(PasswordEncoder passwordEncoder, CompanyService companyService, EmailSender emailSender) {
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.emailSender = emailSender;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyDto companyDto) {
        System.out.println("REGISTER ENDPOINT HIT");
        System.out.println("Email: " + companyDto.getEmail());
        Company company;
        try {
            company = (Company) companyService.loadUserByUsername(companyDto.getEmail());
        } catch (UsernameNotFoundException e) {
            company = null;
        }
        String encodedPassword = passwordEncoder.encode(companyDto.getPassword());
        if(company==null){
            RandomGenerator randomStringGenerator = new RandomGenerator();
            String verificationToken = randomStringGenerator.generateRandomString(7);
            company = new Company(companyDto.getName(), companyDto.getEmail(), encodedPassword, verificationToken, 0);
            //send verification email
            String subject = "Verify Your Email to Complete Login – Company Name";
            String content = new String(
                "Hello "+companyDto.getName()+",\n" + //
                "\n" + //
                                "We noticed a login attempt to your Company Name account.\n" + //
                                "To keep your account secure, please verify your email by clicking the button below:\n" + //
                                "\n" + //
                                frontendUrl + "/auth/verifyEmail/"+companyDto.getEmail()+"/"+verificationToken+"\n" + //verificatoin link
                                "\n" + //
                                "If you did not attempt to log in, you can safely ignore this email.\n" + //
                                "\n" + //
                                "Thank you for being part of Company Name.\n" + //
                                "\n" + //
                                "Warm regards,\n" + //
                                "Team Company Name");
                                emailSender.sendEmail(companyDto.getEmail(), subject, content);
        }
        else if(company!=null && company.getAccountVerified()==1){
            return new ResponseEntity<>("Company already exists with this account. Try login.",HttpStatus.BAD_REQUEST);
        }
        else{
            RandomGenerator randomStringGenerator = new RandomGenerator();
            String verificationToken = randomStringGenerator.generateRandomString(7);
            company.setName(companyDto.getName());
            company.setPassword(encodedPassword);
            company.setVerificationToken(verificationToken);
            //send verification email
            String subject = "Verify Your Email to Complete Login – Company Name";
            String content = new String(
                                "Hello "+companyDto.getName()+",\n" + //
                                "\n" + //
                                "We noticed a login attempt to your Company Name account.\n" + //
                                "To keep your account secure, please verify your email by clicking the button below:\n" + //
                                "\n" + //
                                frontendUrl + "/verifyEmail/"+companyDto.getEmail()+"/"+verificationToken+"\n" + //verificatoin link
                                "\n" + //
                                "If you did not attempt to log in, you can safely ignore this email.\n" + //
                                "\n" + //
                                "Thank you for being part of Company Name.\n" + //
                                "\n" + //
                                "Warm regards,\n" + //
                                "Team Company Name");
            emailSender.sendEmail(companyDto.getEmail(), subject, content);
        }
        companyService.saveCompany(company);
        System.out.println("COMPANY SAVE CALLED");
        return new ResponseEntity<>("Verification email sent, please check.", HttpStatus.OK);
    }

    @GetMapping("/verifyEmail/{email}/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("email") String email, @PathVariable("token") String token) {
        Company company = (Company) companyService.loadUserByUsername(email);
        if (!company.getVerificationToken().equals(token)) {
            return new ResponseEntity<>("<h1>Wrong Token.Please try signup again.</h1>", HttpStatus.BAD_REQUEST);
        }
        company.setAccountVerified(1);
        company.setLastActive(LocalDateTime.now());
        company.setVerificationToken(null);
        companyService.saveCompany(company);
        return new ResponseEntity<>("Account verified successfully.", HttpStatus.OK);
    }

}
