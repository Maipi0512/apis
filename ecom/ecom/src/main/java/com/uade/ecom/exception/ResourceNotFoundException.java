package com.uade.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepcion generica de "no encontrado" (404), reutilizable para
 * cualquier entidad del ecommerce (Direccion, Producto, Usuario, Pedido,
 * DetallePedido, Pago, Proveedor) y tambien para cuando, al crear algo,
 * la referencia (por ejemplo categoriaId o usuarioId) no existe.
 *
 * Category tiene su propia CategoryNotFoundException porque fue la que
 * armamos en la clase; el resto de las entidades nuevas reutilizan esta
 * para no repetir una clase de excepcion por cada tabla.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
