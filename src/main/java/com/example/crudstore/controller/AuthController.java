package com.example.crudstore.controller;

import com.example.crudstore.model.User;
import com.example.crudstore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, 
                          BindingResult result, 
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("email", user.getEmail());
            return "redirect:/verify-otp";
        } catch (Exception e) {
            result.rejectValue("email", "error.user", e.getMessage());
            return "auth/register";
        }
    }
    
    @GetMapping("/verify-otp")
    public String verifyOtpForm(@ModelAttribute("email") String email, Model model) {
        if (email.isEmpty()) {
            return "redirect:/login";
        }
        
        model.addAttribute("email", email);
        return "auth/verify-otp";
    }
    
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, 
                           @RequestParam String otp, 
                           RedirectAttributes redirectAttributes) {
        boolean verified = userService.verifyOtp(email, otp);
        
        if (verified) {
            redirectAttributes.addFlashAttribute("message", "Account verified successfully. Please login.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid OTP or OTP expired");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/verify-otp";
        }
    }
    
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String email, RedirectAttributes redirectAttributes) {
        try {
            userService.resendOtp(email);
            redirectAttributes.addFlashAttribute("message", "OTP resent successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        redirectAttributes.addFlashAttribute("email", email);
        return "redirect:/verify-otp";
    }
}
