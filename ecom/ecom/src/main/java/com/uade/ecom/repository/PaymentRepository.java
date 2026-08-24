package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
