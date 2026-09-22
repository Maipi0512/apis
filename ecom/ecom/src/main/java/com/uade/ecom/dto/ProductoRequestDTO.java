package com.uade.ecom.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * categoriaId es obligatorio. descuentoPorcentaje es opcional: si no se
 * manda (o se manda null), el producto queda sin descuento (0).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoRequestDTO {

    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;
    private BigDecimal descuentoPorcentaje;
}
