package com.uade.ecom.dto;

import java.math.BigDecimal;

import com.uade.ecom.model.ItemCarrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Una linea del carrito, tal como la ve el front dentro de
 * CarritoResponseDTO: nombre y precio (con descuento) del producto ya
 * resueltos, para no obligar a pedirlos aparte.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoResponseDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static ItemCarritoResponseDTO from(ItemCarrito item) {
        BigDecimal precioUnitario = item.getProducto().getPrecioFinal();
        return new ItemCarritoResponseDTO(
                item.getId(),
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                item.getCantidad(),
                precioUnitario,
                precioUnitario.multiply(BigDecimal.valueOf(item.getCantidad())));
    }
}
