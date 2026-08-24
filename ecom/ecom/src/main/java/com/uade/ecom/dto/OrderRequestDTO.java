package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Para crear un pedido, el cliente solo manda el usuario que lo hace.
 * fecha, estado y total los completa el servicio (fecha = hoy,
 * estado = "PENDIENTE", total = 0, y el total se va a ir actualizando
 * a medida que se agreguen DetallePedido).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {

    private Long usuarioId;
}
