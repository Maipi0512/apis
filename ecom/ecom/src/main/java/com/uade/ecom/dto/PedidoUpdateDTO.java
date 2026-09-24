package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para modificar un pedido ya creado. A diferencia de la creacion
 * (POST /pedidos, que va sin body), el update solo permite cambiar el
 * estado (ej: PENDIENTE -> PAGADO -> ENVIADO -> CANCELADO). 
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoUpdateDTO {

    private String estado;
}
