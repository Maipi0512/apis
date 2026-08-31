package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza cuando se pide una categoría (por id) y no se encuentra
 * ninguna coincidencia en la base de datos.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No se encontraron resultados para la búsqueda")
public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
