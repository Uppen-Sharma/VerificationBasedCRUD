package com.example.crudstore.controller;

import com.example.crudstore.model.Cart;
import com.example.crudstore.model.User;
import com.example.crudstore.service.CartService;
import com.example.crudstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    
    @GetMapping("/cart")
    public String viewCart(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Cart cart = cartService.getCartByUser(user);
        model.addAttribute("cart", cart);
        
        return "cart/view";
    }
    
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long productId, 
                           @RequestParam(defaultValue = "1") int quantity,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cartService.addToCart(user, productId, quantity);
            redirectAttributes.addFlashAttribute("message", "Product added to cart successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/product/" + productId;
    }
    
    @PostMapping("/cart/update")
    public String updateCartItem(@RequestParam Long productId, 
                                @RequestParam int quantity,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cartService.updateCartItem(user, productId, quantity);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long productId,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cartService.removeFromCart(user, productId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }
    
    @PostMapping("/cart/clear")
    public String clearCart(Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cartService.clearCart(user);
            redirectAttributes.addFlashAttribute("message", "Cart cleared successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/cart";
    }
}
