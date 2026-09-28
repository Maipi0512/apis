package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para que un usuario edite sus propios datos (PUT /usuarios/me).
 * A proposito NO tiene "rol": si lo tuviera, un CLIENTE podria mandar
 * "rol": "ADMIN" en el body y auto-promoverse, el mismo problema que ya
 * se evito en el registro (ver RegistroRequestDTO). El rol de una
 * cuenta nunca se toca desde un endpoint que el propio dueño llama.
 *
 * Tampoco tiene "password": cambiar la contrasena es una accion mas
 * sensible que deberia pedir la contrasena actual, asi que queda fuera
 * de este PUT (se podria agregar despues un endpoint aparte para eso).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
}
