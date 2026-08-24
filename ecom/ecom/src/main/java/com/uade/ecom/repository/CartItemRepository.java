package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
