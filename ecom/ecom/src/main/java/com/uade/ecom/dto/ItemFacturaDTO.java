package com.uade.ecom.dto;

import java.math.BigDecimal;

import com.uade.ecom.model.UnidadMedida;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Un producto dentro de una compra (sale de un DetallePedido): variante
 * (color y/o numero, null si el producto no tiene), cantidad y en que
 * unidad (2 METRO, 3 UNIDAD...), precio unitario (el que tenia el
 * producto al momento de la compra, no el actual) y el subtotal de esa
 * linea. productoId sirve para linkear a la pagina del
 * producto ("volver a comprar").
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemFacturaDTO {

    private Long productoId;
    private String producto;
    private String color;
    private String numero;
    private Integer cantidad;
    private UnidadMedida unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
