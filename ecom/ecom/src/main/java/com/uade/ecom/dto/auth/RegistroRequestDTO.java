package com.uade.ecom.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * El registro publico NO recibe "rol": todo el que se registra por acá
 * es CLIENTE (ver AutenticacionServiceImpl.registrar()). Si se dejara
 * elegir el rol desde el body, cualquiera podria auto-asignarse ADMIN.
 * El unico ADMIN del sistema lo crea AdminSeeder al arrancar la app.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String direccion;
}
