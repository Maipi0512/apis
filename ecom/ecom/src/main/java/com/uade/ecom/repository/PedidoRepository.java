package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
