package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
