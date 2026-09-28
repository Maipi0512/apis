package com.uade.ecom.dto;

import com.uade.ecom.model.Rol;
import com.uade.ecom.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que devuelven los endpoints de /usuarios: nunca la entidad Usuario
 * directa (aunque la password ya tiene @JsonIgnore, es mas prolijo no
 * depender solo de eso). El rol se puede LEER aca -- saber tu propio
 * rol no es un problema -- pero UsuarioUpdateDTO no lo deja escribir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private Rol rol;

    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDireccion(),
                usuario.getRol());
    }
}
