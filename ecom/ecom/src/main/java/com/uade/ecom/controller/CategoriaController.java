package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CategoriaRequestDTO;
import com.uade.ecom.model.Categoria;
import com.uade.ecom.service.CategoriaService;

/**
 * @RestController marca esta clase como capa de tráfico: acá entra y sale
 * toda la comunicación HTTP.
 *
 * El controller se comunica con CategoriaService, que es una interfaz.
 * Sabe que los métodos existen, pero no sabe qué hacen: esa lógica vive
 * en CategoriaServiceImpl, y es Spring (vía @Autowired) el que se encarga
 * de inyectar la implementación correcta acá adentro.
 */
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> getAllCategorias() {
        return categoriaService.getAllCategorias();
    }

    @GetMapping("/{id}")
    public Categoria getCategoriaById(@PathVariable Long id) {
        return categoriaService.getCategoriaById(id);
    }

    @PostMapping
    public Categoria createCategoria(@RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        return categoriaService.createCategoria(categoriaRequestDTO);
    }

    @PutMapping("/{id}")
    public Categoria updateCategoria(@PathVariable Long id, @RequestBody CategoriaRequestDTO categoriaRequestDTO) {
        return categoriaService.updateCategoria(id, categoriaRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteCategoria(id);
    }
}
