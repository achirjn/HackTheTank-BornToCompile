package com.HTT.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String receiverEmailId, String subject, String content){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(receiverEmailId);
            message.setFrom("no-reply@companyName.com");
            message.setSubject(subject);
            message.setText(content);

            javaMailSender.send(message);
            System.out.println("email sent successfully.");
        } catch(Exception e){
            System.out.println("failed to send email.");
            e.printStackTrace();
        }

    }
}

