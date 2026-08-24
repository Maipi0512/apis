package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
