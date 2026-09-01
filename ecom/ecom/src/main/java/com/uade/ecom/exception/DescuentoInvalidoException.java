package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando el descuentoPorcentaje de un producto viene fuera del
 * rango valido (0 a 100).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DescuentoInvalidoException extends RuntimeException {

    public DescuentoInvalidoException(String message) {
        super(message);
    }
}
