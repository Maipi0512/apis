package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta registrar un pago que no corresponde: sobre
 * un pedido CANCELADO, o por un monto que supera el saldo pendiente.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PagoInvalidoException extends RuntimeException {

    public PagoInvalidoException(String message) {
        super(message);
    }
}
