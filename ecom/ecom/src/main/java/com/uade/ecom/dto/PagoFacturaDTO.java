package com.uade.ecom.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Un pago aplicado al pedido (puede haber mas de uno, ej. pagos parciales).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoFacturaDTO {

    private String metodoPago;
    private BigDecimal monto;
}
