package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoRequestDTO {

    private Long carritoId;
    private Long productoId;
    private Integer cantidad;
}
