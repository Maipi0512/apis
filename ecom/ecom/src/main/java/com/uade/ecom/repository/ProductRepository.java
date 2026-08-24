package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
