package com.uade.ecom.dto.auth;

import com.uade.ecom.model.Rol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequestDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private Rol rol;
    private String direccion;
}
