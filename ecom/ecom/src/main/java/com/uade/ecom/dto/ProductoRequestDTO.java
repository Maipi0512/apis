package com.uade.ecom.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * proveedorId es opcional (nullable = true en Producto); categoriaId es
 * obligatorio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;
    private Long proveedorId;
}
