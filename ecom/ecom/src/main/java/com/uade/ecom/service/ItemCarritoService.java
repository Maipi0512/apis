package com.uade.ecom.service;

import com.uade.ecom.dto.ItemCarritoRequestDTO;

/**
 * Items del carrito del usuario autenticado, identificados por producto
 * y variante (cada combinacion aparece una sola vez en el carrito).
 * varianteId es null para productos sin variantes.
 */
public interface ItemCarritoService {

    /**
     * Agrega el producto (en la variante elegida) al carrito; si ya
     * estaba, le suma la cantidad.
     */
    void agregar(ItemCarritoRequestDTO itemCarritoRequestDTO);

    void cambiarCantidad(Long productoId, Long varianteId, Integer cantidad);

    void quitar(Long productoId, Long varianteId);
}
