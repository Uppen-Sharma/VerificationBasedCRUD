package com.example.crudstore.controller;

import com.example.crudstore.model.Order;
import com.example.crudstore.model.User;
import com.example.crudstore.service.OrderService;
import com.example.crudstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    
    @GetMapping("/checkout")
    public String checkout(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        return "order/checkout";
    }
    
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam String shippingAddress,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            Order order = orderService.createOrder(user, shippingAddress);
            redirectAttributes.addFlashAttribute("message", "Order placed successfully");
            return "redirect:/order/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/checkout";
        }
    }
    
    @GetMapping("/orders")
    public String orderHistory(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        
        return "order/history";
    }
    
    @GetMapping("/order/{id}")
    public String orderDetail(@PathVariable Long id, 
                             Authentication authentication, 
                             Model model,
                             RedirectAttributes redirectAttributes) {
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Order> orderOpt = orderService.getOrderById(id);
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            // Check if the order belongs to the user or user is admin
            if (order.getUser().getId().equals(user.getId()) || user.getRole() == User.Role.ROLE_ADMIN) {
                model.addAttribute("order", order);
                return "order/detail";
            }
        }
        
        redirectAttributes.addFlashAttribute("error", "Order not found");
        return "redirect:/orders";
    }
}
