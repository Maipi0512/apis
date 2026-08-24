package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
