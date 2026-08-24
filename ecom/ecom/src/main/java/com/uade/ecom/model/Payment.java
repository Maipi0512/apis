package com.uade.ecom.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Pago" del DER. Un pedido puede tener uno o mas pagos
 * asociados (por ejemplo, pagos parciales), por eso Pedido 1 -- N Pago.
 */
@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Order order;
}
