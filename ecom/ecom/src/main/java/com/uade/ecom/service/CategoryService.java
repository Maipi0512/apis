package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.CategoryRequestDTO;
import com.uade.ecom.model.Category;

/**
 * Interfaz de la capa de servicio (lógica de negocio) para Category.
 *
 * Los métodos acá están declarados SIN cuerpo: es la abstracción con la
 * que el controller se va a comunicar. El controller conoce que estos
 * métodos existen, pero nunca sabe qué hacen ni cómo están implementados.
 * Eso es lo que rompe el acoplamiento fuerte entre controller y service.
 */
public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category createCategory(CategoryRequestDTO categoryRequestDTO);
}
