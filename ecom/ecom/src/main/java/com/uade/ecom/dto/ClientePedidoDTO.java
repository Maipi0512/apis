package com.uade.ecom.dto;

import com.uade.ecom.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Quien hizo la compra, tal como lo ve el ADMIN en GET /pedidos: lo
 * necesario para despachar el pedido o contactar al cliente. Nunca la
 * password ni el rol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientePedidoDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String direccion;

    public static ClientePedidoDTO from(Usuario usuario) {
        return new ClientePedidoDTO(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getDireccion());
    }
}
