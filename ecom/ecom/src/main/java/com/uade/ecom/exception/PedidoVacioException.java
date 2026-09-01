package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza al intentar marcar como PAGADO un pedido que no tiene ningun
 * DetallePedido cargado -- no tendria sentido cobrar una "venta" vacia.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PedidoVacioException extends RuntimeException {

    public PedidoVacioException(String message) {
        super(message);
    }
}
