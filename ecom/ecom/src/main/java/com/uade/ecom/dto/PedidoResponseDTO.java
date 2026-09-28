package com.uade.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Una compra, tal como la ve el cliente en "Mis compras": numero,
 * fecha, estado, los productos que llevo (sus DetallePedido), como pago
 * y el total. Es tambien la factura del pedido.
 *
 * "cliente" solo se completa cuando consulta un ADMIN (necesita saber a
 * quien despacharle); para un CLIENTE queda null y no aparece en el
 * JSON, porque solo ve sus propias compras.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDTO {

    private Long numeroPedido;
    private LocalDate fecha;
    private String estado;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ClientePedidoDTO cliente;

    private List<ItemFacturaDTO> items;
    private String metodoPago;
    private BigDecimal total;
}
