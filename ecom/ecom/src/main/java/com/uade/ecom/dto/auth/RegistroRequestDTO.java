package com.uade.ecom.dto.auth;

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
}
