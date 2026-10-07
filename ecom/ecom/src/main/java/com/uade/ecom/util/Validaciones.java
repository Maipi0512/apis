package com.uade.ecom.util;

/**
 * Validaciones de datos que se repiten en mas de un service.
 */
public final class Validaciones {

    private Validaciones() {
    }

    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    /**
     * Control basico de formato: algo@algo.algo, sin espacios.
     */
    public static boolean esEmailValido(String email) {
        if (estaVacio(email) || email.contains(" ")) {
            return false;
        }
        int arroba = email.indexOf('@');
        int punto = email.lastIndexOf('.');
        return arroba > 0 && arroba == email.lastIndexOf('@') && punto > arroba + 1 && punto < email.length() - 1;
    }
}
