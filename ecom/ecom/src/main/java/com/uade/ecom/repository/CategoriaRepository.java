package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Categoria;

/**
 * Repositorio de acceso a datos para Categoria.
 *
 * Es una interfaz (no una clase) porque Spring Data JPA trabaja con
 * inyección de dependencias: nosotros no necesitamos saber CÓMO están
 * implementados estos métodos, solo que existen.
 *
 * Extiende de JpaRepository<Categoria, Long>:
 *  - Categoria: la entidad sobre la que vamos a persistir.
 *  - Long: el tipo de dato de la primary key de esa entidad.
 *
 * Al extender JpaRepository heredamos, gratis, todos los métodos de la
 * API de persistencia de Java (findAll, findById, save, deleteById, etc.)
 * que Hibernate implementa por detrás.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}