package com.uade.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.uade.ecom.model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que efectivamente devuelven los endpoints de /pedidos. Nunca el
 * Usuario completo (nombre, email, direccion): solo su id. Antes estos
 * endpoints devolvian la entidad Pedido tal cual, que trae el Usuario
 * dueño anidado -- eso filtraba sus datos de registro en cada
 * GET /pedidos, aunque la password ya tenga @JsonIgnore.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long id;
    private LocalDate fecha;
    private String estado;
    private BigDecimal total;
    private Long usuarioId;

    public static PedidoResponseDTO from(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getUsuario() != null ? pedido.getUsuario().getId() : null);
    }
}
