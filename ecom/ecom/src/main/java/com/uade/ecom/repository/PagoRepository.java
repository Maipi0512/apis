package com.uade.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByPedidoId(Long pedidoId);
}
