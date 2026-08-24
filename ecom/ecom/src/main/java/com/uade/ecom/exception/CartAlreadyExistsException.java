package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se intenta crear un carrito para un usuario que ya
 * tiene uno (la relacion Usuario-Carrito es 1 a 1).
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Este usuario ya tiene un carrito")
public class CartAlreadyExistsException extends RuntimeException {

    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
