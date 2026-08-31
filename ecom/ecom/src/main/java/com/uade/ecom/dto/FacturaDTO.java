package com.uade.ecom.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reporte "factura" de un pedido: junta el pedido con sus DetallePedido
 * (items comprados) y sus Pago (pagos aplicados), mas un par de totales
 * calculados, para poder mostrarlo/imprimirlo de una sola consulta en vez
 * de tener que pedir /pedidos, /detalles-pedido y /pagos por separado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {

    private Long numeroPedido;
    private LocalDate fecha;
    private String estado;

    private List<ItemFacturaDTO> items;
    private BigDecimal total;

    private List<PagoFacturaDTO> pagos;
    private BigDecimal totalPagado;
    private BigDecimal saldoPendiente;
}
