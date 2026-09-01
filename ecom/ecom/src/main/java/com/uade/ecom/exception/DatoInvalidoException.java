package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando un dato numerico de negocio (precio, stock, cantidad,
 * monto) no tiene un valor valido -- ej: negativo, o cero donde no
 * corresponde.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DatoInvalidoException extends RuntimeException {

    public DatoInvalidoException(String message) {
        super(message);
    }
}
