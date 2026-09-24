package com.uade.ecom.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad "Usuario" del DER.
 *
 * Implementa UserDetails para que Spring Security pueda autenticar
 * directamente contra esta entidad, sin necesitar una clase intermedia.
 * El "username" que usa Spring Security es el email (ver getUsername()).
 */
@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // @JsonIgnore: el hash de la password nunca tiene que viajar en un
    // JSON de respuesta, ni siquiera cuando Usuario aparece anidado
    // dentro de otra entidad (Pedido.usuario, Carrito.usuario, etc.).
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    // Nullable a proposito: no todos los usuarios cargan direccion al
    // registrarse (ej. un ADMIN no la necesita). Es un campo simple, no
    // una entidad aparte -- la tabla "direccion" del diseño original
    // quedo sin uso, se reemplaza por esto.
    @Column(name = "direccion")
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;

    // El resto de los metodos de UserDetails son detalles de
    // implementacion de Spring Security (no datos del negocio): se
    // ignoran en el JSON para no ensuciar las respuestas de la API.
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return email;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
