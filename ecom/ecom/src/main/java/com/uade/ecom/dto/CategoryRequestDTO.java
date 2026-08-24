package com.uade.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que modela lo que el cliente manda por body cuando quiere crear
 * una categoria nueva. No incluye el id porque ese lo genera la base de
 * datos (ver Category -> @GeneratedValue).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    private String nombre;
}
