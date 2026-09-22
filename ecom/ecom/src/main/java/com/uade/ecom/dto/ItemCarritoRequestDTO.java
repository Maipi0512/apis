package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * No lleva carritoId: el item siempre se agrega al carrito del usuario
 * autenticado (ver ItemCarritoServiceImpl), no a uno que elija el cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoRequestDTO {

    private Long productoId;
    private Integer cantidad;
}
