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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Una variacion de un producto con su propio stock. Segun el producto
 * se diferencia por color (hilo "Rojo"), por numero (aguja "3.5") o por
 * las dos cosas (boton "Negro" "15 mm"): al menos uno de los dos tiene
 * que estar. El precio es siempre el del producto: la variante solo
 * cambia el stock. El stock del producto es la suma de sus variantes
 * (ver Producto.recalcularStock()).
 */
@Entity
@Table(name = "variante_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variante")
    private Long id;

    @Column(name = "color")
    private String color;

    // Texto y no numero: puede ser "3", "3.5", "15 mm", "20 cm"...
    @Column(name = "numero")
    private String numero;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    // @JsonIgnore: la variante se muestra adentro de su producto, no hace
    // falta repetir el producto (y evita el ciclo producto -> variantes ->
    // producto al serializar).
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /**
     * Como se muestra la variante: "Rojo", "Nº 3.5" o "Negro - Nº 15 mm".
     */
    @Transient
    public String getDescripcion() {
        if (color != null && numero != null) {
            return color + " - Nº " + numero;
        }
        return color != null ? color : "Nº " + numero;
    }
}
