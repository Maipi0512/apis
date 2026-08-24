package com.uade.ecom.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Usuario" del DER. Se llama User en el codigo (convencion en
 * ingles) pero mapea a la tabla "usuario" -- ademas "user" es palabra
 * reservada en Postgres, asi que el @Table es necesario, no solo
 * prolijidad.
 *
 * "rol" queda como un simple String por ahora (ej: "CLIENTE", "ADMIN").
 * Cuando lleguen a la parte de seguridad de la materia, ese campo es el
 * que probablemente termine conectado a Spring Security.
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "rol", nullable = false)
    private String rol;

    /**
     * Relacion "Tiene" del DER: 1 a 1 entre Usuario y Direccion.
     */
    @OneToOne
    @JoinColumn(name = "direccion_id")
    private Address address;
}
