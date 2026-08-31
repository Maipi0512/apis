package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Se lanza al intentar borrar una entidad (Producto, Categoria,
 * Proveedor) que todavia esta referenciada por otra (ej: un Producto que
 * aparece en algun DetallePedido) -- borrarla rompería esas referencias.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadEnUsoException extends RuntimeException {

    public EntidadEnUsoException(String message) {
        super(message);
    }
}
