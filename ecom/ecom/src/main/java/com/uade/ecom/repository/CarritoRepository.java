package com.uade.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuarioId(Long usuarioId);
}
