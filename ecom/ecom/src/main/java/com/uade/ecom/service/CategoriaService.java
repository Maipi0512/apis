package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.CategoriaRequestDTO;
import com.uade.ecom.model.Categoria;

/**
 * Interfaz de la capa de servicio (lógica de negocio) para Categoria.
 *
 * Los métodos acá están declarados SIN cuerpo: es la abstracción con la
 * que el controller se va a comunicar. El controller conoce que estos
 * métodos existen, pero nunca sabe qué hacen ni cómo están implementados.
 * Eso es lo que rompe el acoplamiento fuerte entre controller y service.
 */
public interface CategoriaService {

    List<Categoria> getAllCategorias();

    Categoria getCategoriaById(Long id);

    Categoria createCategoria(CategoriaRequestDTO categoriaRequestDTO);

    Categoria updateCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO);

    void deleteCategoria(Long id);
}
