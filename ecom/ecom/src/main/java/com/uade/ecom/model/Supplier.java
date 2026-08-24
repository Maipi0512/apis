package com.uade.ecom.model;

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
 * Entidad "Proveedor" del DER.
 */
@Entity
@Table(name = "proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "contacto")
    private String contacto;
}
