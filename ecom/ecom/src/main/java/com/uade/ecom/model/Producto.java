package com.uade.ecom.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
 * Entidad "Producto" del DER.
 *
 * Relaciones:
 *  - "Clasifica" (Categoria 1:N Producto) -> categoria (ManyToOne).
 *  - "Distribuye" (Proveedor 1:N Producto) -> proveedor (ManyToOne).
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

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


    // columnDefinition con "default 0" para que, si la tabla ya tiene
    // productos cargados, el ALTER TABLE de Hibernate (ddl-auto=update) no
    // falle por violar el NOT NULL en las filas existentes.
    @Column(name = "descuento_porcentaje", nullable = false, columnDefinition = "numeric(5,2) default 0")
    private BigDecimal descuentoPorcentaje = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    // No se guarda en la base (@Transient): se calcula al vuelo cada vez
    // que se serializa el producto a JSON, así el front siempre ve el
    // precio ya con el descuento aplicado sin tener que calcularlo el.
    @Transient
    public BigDecimal getPrecioFinal() {
        BigDecimal descuento = precio
                .multiply(descuentoPorcentaje)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return precio.subtract(descuento);
    }
}
