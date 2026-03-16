package com.example.crudstore.service;

import com.example.crudstore.model.Cart;
import com.example.crudstore.model.User;
import com.example.crudstore.repository.CartRepository;
import com.example.crudstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    
    @Value("${app.otp.expiration-minutes}")
    private int otpExpirationMinutes;
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.ROLE_USER);
        user.setEnabled(false);
        
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // Create cart for the user
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);
        
        // Send OTP email
        emailService.sendOtpEmail(user.getEmail(), user.getName(), otp);
        
        return savedUser;
    }
    
    @Transactional
    public boolean verifyOtp(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            if (user.getOtp().equals(otp)) {
                LocalDateTime otpGeneratedTime = user.getOtpGeneratedTime();
                LocalDateTime now = LocalDateTime.now();
                
                if (now.isBefore(otpGeneratedTime.plusMinutes(otpExpirationMinutes))) {
                    user.setEnabled(true);
                    user.setOtp(null);
                    user.setOtpGeneratedTime(null);
                    userRepository.save(user);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    @Transactional
    public void resendOtp(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            
            String otp = generateOTP();
            user.setOtp(otp);
            user.setOtpGeneratedTime(LocalDateTime.now());
            userRepository.save(user);
            
            emailService.sendOtpEmail(user.getEmail(), user.getName(), otp);
        } else {
            throw new RuntimeException("User not found");
        }
    }
    
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
    
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
