package com.ethreumTracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSuccessEmail(String recipientEmail, String messageBody) {
        try {
           
            SimpleMailMessage message = new SimpleMailMessage();
            
            message.setTo(recipientEmail); 
            message.setSubject("Deposit Tracking Successful");
            message.setText(messageBody);
            message.setFrom("asbijutkar045@gmail.com");
            emailSender.send(message);
            
            System.out.println("Email sent successfully!");
        } catch (Exception e) {
            System.err.println("Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

