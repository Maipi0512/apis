package com.uade.ecom.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Una linea de la factura: producto comprado, cantidad, precio unitario
 * (el que tenia el producto al momento de la compra, no el actual) y el
 * subtotal de esa linea.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFacturaDTO {

    private String producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
