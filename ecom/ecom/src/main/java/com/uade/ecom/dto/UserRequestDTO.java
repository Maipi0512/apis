package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * direccionId es opcional: un usuario puede crearse sin direccion todavia
 * y cargarla despues.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String nombre;
    private String email;
    private String rol;
    private Long direccionId;
}
