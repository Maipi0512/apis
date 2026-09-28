package com.uade.ecom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Alta/edicion de una variante de un producto (solo ADMIN). El producto
 * va en la URL: /productos/{productoId}/variantes. color y numero son
 * opcionales cada uno, pero tiene que venir al menos uno.
 */
@Data
@NoArgsConstructor
public class VarianteProductoRequestDTO {

    private String color;
    private String numero;
    private Integer stock;
}
