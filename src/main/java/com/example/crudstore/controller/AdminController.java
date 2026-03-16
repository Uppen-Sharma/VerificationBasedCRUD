package com.example.crudstore.controller;

import com.example.crudstore.model.Order;
import com.example.crudstore.model.Product;
import com.example.crudstore.model.User;
import com.example.crudstore.service.OrderService;
import com.example.crudstore.service.ProductService;
import com.example.crudstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    
    @GetMapping
    public String dashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Order> orders = orderService.getAllOrders();
        List<User> users = userService.getAllUsers();
        
        model.addAttribute("productCount", products.size());
        model.addAttribute("orderCount", orders.size());
        model.addAttribute("userCount", users.size());
        model.addAttribute("recentOrders", orders.stream().limit(5).toList());
        
        return "admin/dashboard";
    }
    
    // Product Management
    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "admin/products/list";
    }
    
    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/products/form";
    }
    
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> productOpt = productService.getProductById(id);
        
        if (productOpt.isPresent()) {
            model.addAttribute("product", productOpt.get());
            return "admin/products/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Product not found");
            return "redirect:/admin/products";
        }
    }
    
    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product, 
                             @RequestParam(required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/products");
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Files.copy(image.getInputStream(), uploadPath.resolve(fileName));
                product.setImageUrl("/images/products/" + fileName);
            }
            
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("message", "Product saved successfully");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to save product: " + e.getMessage());
        }
        
        return "redirect:/admin/products";
    }
    
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete product: " + e.getMessage());
        }
        
        return "redirect:/admin/products";
    }
    
    // Order Management
    @GetMapping("/orders")
    public String orders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/orders/list";
    }
    
    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Order> orderOpt = orderService.getOrderById(id);
        
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            model.addAttribute("statuses", Order.OrderStatus.values());
            return "admin/orders/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "Order not found");
            return "redirect:/admin/orders";
        }
    }
    
    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam Long orderId, 
                                   @RequestParam Order.OrderStatus status,
                                   RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(orderId, status);
            redirectAttributes.addFlashAttribute("message", "Order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update order status: " + e.getMessage());
        }
        
        return "redirect:/admin/orders/" + orderId;
    }
    
    // User Management
    @GetMapping("/users")
    public String users(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }
    
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.getUserById(id);
        
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            model.addAttribute("orders", orderService.getOrdersByUser(userOpt.get()));
            return "admin/users/detail";
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found");
            return "redirect:/admin/users";
        }
    }
}
