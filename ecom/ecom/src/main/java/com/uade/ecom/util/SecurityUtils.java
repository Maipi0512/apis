package com.uade.ecom.util;

import org.springframework.security.core.context.SecurityContextHolder;

import com.uade.ecom.model.Rol;
import com.uade.ecom.model.Usuario;

/**
 * Acceso rapido al Usuario autenticado actual: el JwtAutenticacionFilter
 * ya deja el Usuario completo (no solo el email) como principal en el
 * SecurityContext, asi que no hace falta ir a buscarlo de nuevo al
 * repository.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Usuario getUsuarioActual() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean esAdmin() {
        return getUsuarioActual().getRol() == Rol.ADMIN;
    }
}
