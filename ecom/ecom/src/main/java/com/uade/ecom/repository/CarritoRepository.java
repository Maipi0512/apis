package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
}
