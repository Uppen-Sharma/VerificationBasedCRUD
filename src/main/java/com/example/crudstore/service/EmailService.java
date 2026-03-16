package com.example.crudstore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Async
    public void sendOtpEmail(String to, String name, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("otp", otp);
            
            String htmlContent = templateEngine.process("email/otp-email", context);
            
            helper.setTo(to);
            helper.setSubject("CRUD Store - Your OTP Verification Code");
            helper.setFrom("binay143h@gmail.com");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
    
    @Async
    public void sendOrderConfirmationEmail(String to, String name, Long orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("orderId", orderId);
            
            String htmlContent = templateEngine.process("email/order-confirmation", context);
            
            helper.setTo(to);
            helper.setSubject("CRUD Store - Order Confirmation #" + orderId);
            helper.setFrom("binay143h@gmail.com");
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send order confirmation email", e);
        }
    }
}
