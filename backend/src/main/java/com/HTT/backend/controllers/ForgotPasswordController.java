package com.HTT.backend.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.HTT.backend.entities.Company;
import com.HTT.backend.helper.RandomGenerator;
import com.HTT.backend.services.CompanyService;
import com.HTT.backend.services.impl.EmailSender;

import jakarta.servlet.http.HttpSession;

@RestController
public class ForgotPasswordController {

    private PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private EmailSender emailSender;
    @Value("${frontend.base.url}")
    private String frontendBaseUrl;

    public ForgotPasswordController(PasswordEncoder passwordEncoder, CompanyService companyService, EmailSender emailSender) {
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.emailSender = emailSender;
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email, HttpSession session) {
        Company company;
        try {
            company = (Company) companyService.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("No company found with this email.", HttpStatus.BAD_REQUEST);
        }
        String token = RandomGenerator.generateRandomString(10);
        String subject = "Password Reset Link for Think India SVNIT";
        String content = new String(
                "Hello " + company.getName() + ",\n" + //
                        "\n" + //
                        "We received a request to reset your password for your Think India SVNIT account.\n" + //
                        "To proceed with resetting your password, please click the following link:\n" + //
                        "\n" + //
                        frontendBaseUrl + "/reset-password?token=" + token +
                        "\n" + //
                        "\nThis OTP is valid for the next 10 minutes. If you did not request a password reset, please ignore this email.\n"
                        + //
                        "\n" + //
                        "Thank you for being part of Think India SVNIT.\n" + //
                        "\n" + //
                        "Warm regards,\n" + //
                        "Team Think India SVNIT");
        emailSender.sendEmail(email, subject, content);
        company.setResetPasswordToken(token);
        company.setTokenExpirationTime(LocalDateTime.now().plusMinutes(10));
        companyService.saveCompany(company);
        return new ResponseEntity<>("Password reset link sent to your email, please check.", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {

        Company company = companyService.findByResetPasswordToken(token);

        if (company == null ||
                company.getTokenExpirationTime().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }

        company.setPassword(passwordEncoder.encode(newPassword));
        company.setResetPasswordToken(null);
        company.setTokenExpirationTime(null);
        companyService.saveCompany(company);

        return ResponseEntity.ok("Password updated successfully");
    }

}
