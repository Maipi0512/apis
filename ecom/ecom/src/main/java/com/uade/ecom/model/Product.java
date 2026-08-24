package com.uade.ecom.model;

import java.math.BigDecimal;

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
 * Entidad "Producto" del DER.
 *
 * Relaciones:
 *  - "Clasifica" (Categoria 1:N Producto) -> category (ManyToOne).
 *  - "Distribuye" (Proveedor 1:N Producto) -> supplier (ManyToOne).
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Supplier supplier;
}
