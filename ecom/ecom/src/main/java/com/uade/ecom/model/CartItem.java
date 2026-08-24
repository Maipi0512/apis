package com.uade.ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "ItemCarrito": cada linea de un carrito (un producto +
 * cantidad, todavia sin confirmar la compra).
 */
@Entity
@Table(name = "item_carrito")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_carrito")
    private Long id;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;
}
