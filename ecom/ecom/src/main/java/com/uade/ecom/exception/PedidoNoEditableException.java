package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza al intentar agregar, modificar o quitar un DetallePedido de un
 * pedido que ya no esta PENDIENTE (ya se pago, se envio, etc.) -- tocarlo
 * en ese punto rompe el total ya cobrado/facturado.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PedidoNoEditableException extends RuntimeException {

    public PedidoNoEditableException(String message) {
        super(message);
    }
}
