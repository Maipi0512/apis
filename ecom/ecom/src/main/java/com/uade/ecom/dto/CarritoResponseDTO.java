package com.uade.ecom.dto;

import com.uade.ecom.model.Carrito;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que efectivamente devuelven los endpoints de /carritos: solo el id
 * del dueño, nunca el Usuario completo anidado. Ver PedidoResponseDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {

    private Long id;
    private Long usuarioId;

    public static CarritoResponseDTO from(Carrito carrito) {
        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getUsuario() != null ? carrito.getUsuario().getId() : null);
    }
}
