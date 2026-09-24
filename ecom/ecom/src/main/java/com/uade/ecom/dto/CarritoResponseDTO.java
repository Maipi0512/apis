package com.uade.ecom.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.ItemCarrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que efectivamente devuelven los endpoints de /carritos: solo el id
 * del dueño, nunca el Usuario completo anidado (ver PedidoResponseDTO),
 * mas sus items. Si no tiene items, "mensaje" avisa que esta vacio; si
 * tiene, "items" trae cada linea con el producto y el subtotal.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {

    private Long id;
    private Long usuarioId;
    private String mensaje;
    private List<ItemCarritoResponseDTO> items;

    public static CarritoResponseDTO from(Carrito carrito, List<ItemCarrito> itemsCarrito) {
        Long usuarioId = carrito.getUsuario() != null ? carrito.getUsuario().getId() : null;

        if (itemsCarrito == null || itemsCarrito.isEmpty()) {
            return new CarritoResponseDTO(carrito.getId(), usuarioId, "El carrito esta vacio", Collections.emptyList());
        }

        List<ItemCarritoResponseDTO> items = itemsCarrito.stream()
                .map(ItemCarritoResponseDTO::from)
                .collect(Collectors.toList());
        return new CarritoResponseDTO(carrito.getId(), usuarioId, null, items);
    }
}
