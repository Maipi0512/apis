package com.uade.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // "Usuario_Id" con guion bajo: fuerza a Spring Data a navegar
    // usuario.id, para no confundirse con el getUsuarioId() transient
    // que Pedido expone solo para el JSON de salida.
    List<Pedido> findByUsuario_Id(Long usuarioId);
}
