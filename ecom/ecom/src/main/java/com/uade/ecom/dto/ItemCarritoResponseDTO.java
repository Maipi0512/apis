package com.uade.ecom.dto;

import java.math.BigDecimal;

import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.UnidadMedida;
import com.uade.ecom.model.VarianteProducto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Una linea del carrito, tal como la ve el front dentro de
 * CarritoResponseDTO: nombre, variante (color/numero) y precio (con
 * descuento) del producto ya resueltos, para no obligar a pedirlos
 * aparte. No lleva el id de la linea: cada producto+variante aparece una
 * sola vez en el carrito, asi que la linea se identifica por productoId
 * (+ varianteId). unidadMedida dice en que se mide la cantidad (2 METRO,
 * 3 UNIDAD...).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoResponseDTO {

    private Long productoId;
    private String productoNombre;
    private Long varianteId;
    private String color;
    private String numero;
    private Integer cantidad;
    private UnidadMedida unidadMedida;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static ItemCarritoResponseDTO from(ItemCarrito item) {
        BigDecimal precioUnitario = item.getProducto().getPrecioFinal();
        VarianteProducto variante = item.getVariante();
        return new ItemCarritoResponseDTO(
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                variante != null ? variante.getId() : null,
                variante != null ? variante.getColor() : null,
                variante != null ? variante.getNumero() : null,
                item.getCantidad(),
                item.getProducto().getUnidadMedida(),
                precioUnitario,
                precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad())));
    }
}
