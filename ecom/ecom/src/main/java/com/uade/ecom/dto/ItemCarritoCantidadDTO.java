package com.uade.ecom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Body de PUT /carritos/items/{productoId}: el producto ya va en la URL,
 * aca solo viaja la cantidad nueva.
 */
@Data
@NoArgsConstructor
public class ItemCarritoCantidadDTO {

    private Integer cantidad;
}
