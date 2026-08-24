package com.uade.ecom.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Pedido" del DER (la orden de compra, distinta del carrito).
 * Se llama Order en el codigo -- "order" es palabra reservada en
 * Postgres, por eso el @Table(name = "pedido") es obligatorio, no solo
 * prolijidad.
 *
 * NOTA: por ahora NO tiene relacion con Usuario. La relacion "Realiza"
 * del DER (Usuario 1 -- N Pedido) se va a agregar cuando trabajen la
 * parte de seguridad y tengan un Usuario real con login.
 */
@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "total", nullable = false)
    private BigDecimal total;
}
