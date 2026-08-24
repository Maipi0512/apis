package com.uade.ecom.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {

    private Long pedidoId;
    private String metodoPago;
    private BigDecimal monto;
}
