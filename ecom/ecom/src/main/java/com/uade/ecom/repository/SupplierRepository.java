package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
