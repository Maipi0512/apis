package com.uade.ecom.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Metodos de pago que acepta la tienda. En la tabla pago se guarda el
 * name() como texto (columna metodo_pago), asi que agregar uno nuevo no
 * requiere tocar la base.
 */
public enum MetodoPago {
    TARJETA_CREDITO,
    TARJETA_DEBITO,
    TRANSFERENCIA,
    EFECTIVO;

    /**
     * Acepta lo que venga del front sin importar mayusculas ni si usa
     * espacios o guiones ("Tarjeta credito", "tarjeta-debito", etc.).
     */
    public static Optional<MetodoPago> parse(String valor) {
        if (valor == null) {
            return Optional.empty();
        }
        String normalizado = valor.trim().toUpperCase().replace(' ', '_').replace('-', '_');
        return Arrays.stream(values())
                .filter(m -> m.name().equals(normalizado))
                .findFirst();
    }
}
