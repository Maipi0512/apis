package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta cambiar el estado de un pedido a uno al que
 * no puede pasar desde su estado actual (ej: de ENTREGADO a PENDIENTE).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class TransicionEstadoInvalidaException extends RuntimeException {

    public TransicionEstadoInvalidaException(String message) {
        super(message);
    }
}
