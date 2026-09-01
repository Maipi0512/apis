package com.uade.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);
}
