package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando ya existe una categoría con la misma descripción.
 * @ResponseStatus hace que, apenas se tire esta excepción, Spring arme
 * automáticamente la respuesta HTTP con el código y el mensaje que le
 * pasamos acá, sin que el controller tenga que saber nada de esto.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "La categoría que se intenta agregar ya existe")
public class CategoriaDuplicadaException extends RuntimeException {

    public CategoriaDuplicadaException(String message) {
        super(message);
    }
}
