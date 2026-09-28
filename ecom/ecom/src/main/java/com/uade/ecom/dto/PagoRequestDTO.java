package com.uade.ecom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo unico que elige el cliente al pagar es el metodo de pago. El monto
 * sale del carrito del usuario autenticado y el pedido se crea recien
 * cuando el pago se registra (como en Mercado Pago), asi que ni monto ni
 * pedidoId vienen en el body.
 */
@Data
@NoArgsConstructor
public class PagoRequestDTO {

    private String metodoPago;
}
