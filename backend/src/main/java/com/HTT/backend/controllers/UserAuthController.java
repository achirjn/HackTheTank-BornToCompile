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

import com.HTT.backend.dto.UserDto;
import com.HTT.backend.entities.User;
import com.HTT.backend.helper.RandomGenerator;
import com.HTT.backend.services.UserService;
import com.HTT.backend.services.impl.EmailSender;



@RestController
@RequestMapping("/auth")
public class UserAuthController {
    
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private EmailSender emailSender;

    @Value("${frontend.base.url}")      
    private String frontendUrl;

    public UserAuthController(PasswordEncoder passwordEncoder, UserService userService, EmailSender emailSender) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.emailSender = emailSender;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        User user;
        try {
            user = (User) userService.loadUserByUsername(userDto.getEmail());
        } catch (UsernameNotFoundException e) {
            user = null;
        }
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        if(user==null){
            RandomGenerator randomStringGenerator = new RandomGenerator();
            String verificationToken = randomStringGenerator.generateRandomString(7);
            user = new User(userDto.getName(), userDto.getEmail(), encodedPassword,verificationToken, 0);
            //send verification email
            String subject = "Verify Your Email to Complete Login – Company Name";
            String content = new String(
                "Hello "+userDto.getName()+",\n" + //
                "\n" + //
                                "We noticed a login attempt to your Company Name account.\n" + //
                                "To keep your account secure, please verify your email by clicking the button below:\n" + //
                                "\n" + //
                                frontendUrl + "/auth/verifyEmail/"+userDto.getEmail()+"/"+verificationToken+"\n" + //verificatoin link
                                "\n" + //
                                "If you did not attempt to log in, you can safely ignore this email.\n" + //
                                "\n" + //
                                "Thank you for being part of Company Name.\n" + //
                                "\n" + //
                                "Warm regards,\n" + //
                                "Team Company Name");
                                emailSender.sendEmail(userDto.getEmail(), subject, content);
        }
        else if(user!=null && user.getAccountVerified()==1){
            return new ResponseEntity<>("User already exists with this account. Try login.",HttpStatus.BAD_REQUEST);
        }
        else{
            RandomGenerator randomStringGenerator = new RandomGenerator();
            String verificationToken = randomStringGenerator.generateRandomString(7);
            user.setName(userDto.getName());
            user.setPassword(encodedPassword);
            user.setVerificationToken(verificationToken);
            //send verification email
            String subject = "Verify Your Email to Complete Login – Company Name";
            String content = new String(
                                "Hello "+userDto.getName()+",\n" + //
                                "\n" + //
                                "We noticed a login attempt to your Company Name account.\n" + //
                                "To keep your account secure, please verify your email by clicking the button below:\n" + //
                                "\n" + //
                                frontendUrl + "/verifyEmail/"+userDto.getEmail()+"/"+verificationToken+"\n" + //verificatoin link
                                "\n" + //
                                "If you did not attempt to log in, you can safely ignore this email.\n" + //
                                "\n" + //
                                "Thank you for being part of Company Name.\n" + //
                                "\n" + //
                                "Warm regards,\n" + //
                                "Team Company Name");
            emailSender.sendEmail(userDto.getEmail(), subject, content);
        }
        userService.saveUser(user);
        return new ResponseEntity<>("Verification email sent, please check.", HttpStatus.OK);
    }
                        
                        
    @GetMapping("/verifyEmail/{email}/{token}")
    public ResponseEntity<?> getMethodName(@PathVariable("email") String email, @PathVariable("token") String token) {
        User user = (User)userService.loadUserByUsername(email);
        if( !user.getVerificationToken().equals(token)){
            return new ResponseEntity<>("<h1>Wrong Token.Please try signup again.</h1>", HttpStatus.BAD_REQUEST);
        }
        user.setAccountVerified(1);
        user.setLastActive(LocalDateTime.now());
        user.setVerificationToken(null);
        userService.saveUser(user);
        return new ResponseEntity<>("Account verified successfully.",HttpStatus.OK);
    }

    
    
}

