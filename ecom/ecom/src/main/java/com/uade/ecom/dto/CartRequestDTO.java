package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Para crear un carrito solo hace falta el usuario dueño. Un usuario no
 * puede tener dos carritos (el service lo valida).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDTO {

    private Long usuarioId;
}
