package com.uade.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Carrito" (no estaba en el DER original, es lo que el usuario
 * arma antes de confirmar la compra, a diferencia de Pedido).
 *
 * Ya con seguridad andando, el Carrito queda asociado al Usuario
 * autenticado que lo crea (ver CarritoServiceImpl.createCarrito()).
 */
@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;

    // No se expone el Usuario completo en el JSON (nombre, email, etc.):
    // alcanza con el id para que el front sepa de quien es el carrito.
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Transient
    public Long getUsuarioId() {
        return usuario == null ? null : usuario.getId();
    }
}
