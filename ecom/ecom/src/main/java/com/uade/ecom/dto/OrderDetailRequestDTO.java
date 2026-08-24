package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * El cliente manda pedidoId, productoId y cantidad. El precioUnitario NO
 * lo manda el cliente: lo toma el servicio del precio actual del
 * producto (asi el usuario nunca puede "inventar" un precio).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDTO {

    private Long pedidoId;
    private Long productoId;
    private Integer cantidad;
}
