package com.uade.ecom.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.uade.ecom.model.ItemCarrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que devuelve GET /carritos: los items del carrito del usuario
 * autenticado y el total a pagar. No lleva el id del carrito ni el del
 * usuario -- son datos internos de la base, al cliente no le sirven
 * (siempre opera sobre "su" carrito, el del token). Si no tiene items,
 * "mensaje" avisa que esta vacio.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {

    private String mensaje;
    private List<ItemCarritoResponseDTO> items;
    private BigDecimal total;

    public static CarritoResponseDTO from(List<ItemCarrito> itemsCarrito) {
        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            return new CarritoResponseDTO("El carrito esta vacio", Collections.emptyList(), BigDecimal.ZERO);
        }

        List<ItemCarritoResponseDTO> items = itemsCarrito.stream()
                .map(ItemCarritoResponseDTO::from)
                .collect(Collectors.toList());
        BigDecimal total = items.stream()
                .map(ItemCarritoResponseDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CarritoResponseDTO(null, items, total);
    }
}
