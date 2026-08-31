package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza al intentar hacer checkout de un carrito que no tiene items.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CarritoVacioException extends RuntimeException {

    public CarritoVacioException(String message) {
        super(message);
    }
}
