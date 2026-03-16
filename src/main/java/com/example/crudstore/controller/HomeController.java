package com.example.crudstore.controller;

import com.example.crudstore.model.Product;
import com.example.crudstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        List<Product> products = productService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "search-results";
    }
    
    @GetMapping("/category")
    public String category(@RequestParam String name, Model model) {
        List<Product> products = productService.getProductsByCategory(name);
        model.addAttribute("products", products);
        model.addAttribute("category", name);
        return "category";
    }
}
