package com.uade.ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Carrito" (no estaba en el DER original, es lo que el usuario
 * arma antes de confirmar la compra, a diferencia de Pedido).
 *
 * NOTA: por ahora NO tiene relacion con Usuario -- eso se agrega cuando
 * armen la parte de seguridad.
 */
@Entity
@Table(name = "carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long id;
}
