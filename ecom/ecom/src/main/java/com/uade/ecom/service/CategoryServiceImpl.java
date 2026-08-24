package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.CategoryRequestDTO;
import com.uade.ecom.exception.CategoryDuplicadaException;
import com.uade.ecom.exception.CategoryNotFoundException;
import com.uade.ecom.model.Category;
import com.uade.ecom.repository.CategoryRepository;

/**
 * Implementacion de CategoryService: aca vive la logica de negocio.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "No se encontro ninguna categoria con id " + id));
    }

    @Override
    public Category createCategory(CategoryRequestDTO categoryRequestDTO) {
        boolean existeDuplicada = categoryRepository.findAll().stream()
                .anyMatch(category -> category.getNombre()
                        .equalsIgnoreCase(categoryRequestDTO.getNombre()));

        if (existeDuplicada) {
            throw new CategoryDuplicadaException(
                    "La categoria que se intenta agregar ya existe: " + categoryRequestDTO.getNombre());
        }

        Category nuevaCategoria = new Category();
        nuevaCategoria.setNombre(categoryRequestDTO.getNombre());

        return categoryRepository.save(nuevaCategoria);
    }
}
