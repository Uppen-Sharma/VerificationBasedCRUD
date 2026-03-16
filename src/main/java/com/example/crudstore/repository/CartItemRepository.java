package com.example.crudstore.repository;

import com.example.crudstore.model.Cart;
import com.example.crudstore.model.CartItem;
import com.example.crudstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
