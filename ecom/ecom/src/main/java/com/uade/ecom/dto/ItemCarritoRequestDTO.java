package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * No lleva carritoId: el item siempre se agrega al carrito del usuario
 * autenticado (ver ItemCarritoServiceImpl), no a uno que elija el cliente.
 *
 * varianteId (el color/numero elegido) es obligatorio si el producto
 * tiene variantes (sale de la lista "variantes" del GET /productos/{id})
 * y no se manda si no tiene.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoRequestDTO {

    private Long productoId;
    private Long varianteId;
    private Integer cantidad;
}
