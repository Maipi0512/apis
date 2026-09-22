package com.uade.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // "Usuario_Id" con guion bajo: fuerza a Spring Data a navegar
    // usuario.id, para no confundirse con el getUsuarioId() transient
    // que Carrito expone solo para el JSON de salida.
    List<Carrito> findByUsuario_Id(Long usuarioId);

    // El "principal" de un usuario: el mas viejo, que es el que se le
    // crea automaticamente al registrarse (ver AutenticacionServiceImpl).
    Optional<Carrito> findFirstByUsuario_IdOrderByIdAsc(Long usuarioId);
}
