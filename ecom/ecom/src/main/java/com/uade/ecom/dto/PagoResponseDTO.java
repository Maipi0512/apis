package com.uade.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.uade.ecom.model.Pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lo que devuelven los endpoints de /pagos: el comprobante del pago con
 * el numero de pedido que genero (la factura completa sale de
 * GET /pedidos/{numeroPedido}). No expone el Usuario ni ids internos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponseDTO {

    private Long numeroPedido;
    private LocalDate fecha;
    private String metodoPago;
    private BigDecimal monto;
    private String estadoPedido;

    public static PagoResponseDTO from(Pago pago) {
        return new PagoResponseDTO(
                pago.getPedido().getId(),
                pago.getPedido().getFecha(),
                pago.getMetodoPago(),
                pago.getMonto(),
                pago.getPedido().getEstado());
    }
}
