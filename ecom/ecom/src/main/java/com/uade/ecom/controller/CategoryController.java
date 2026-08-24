package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CategoryRequestDTO;
import com.uade.ecom.model.Category;
import com.uade.ecom.service.CategoryService;

/**
 * @RestController marca esta clase como capa de tráfico: acá entra y sale
 * toda la comunicación HTTP.
 *
 * El controller se comunica con CategoryService, que es una interfaz.
 * Sabe que los métodos existen, pero no sabe qué hacen: esa lógica vive
 * en CategoryServiceImpl, y es Spring (vía @Autowired) el que se encarga
 * de inyectar la implementación correcta acá adentro.
 */
@RestController
@RequestMapping("/categorias")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.createCategory(categoryRequestDTO);
    }
}
