package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.CategoriaRequestDTO;
import com.uade.ecom.exception.CategoriaDuplicadaException;
import com.uade.ecom.exception.CategoriaNotFoundException;
import com.uade.ecom.model.Categoria;
import com.uade.ecom.repository.CategoriaRepository;

/**
 * Implementacion de CategoriaService: aca vive la logica de negocio.
 */
@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(
                        "No se encontro ninguna categoria con id " + id));
    }

    @Override
    public Categoria createCategoria(CategoriaRequestDTO categoriaRequestDTO) {
        boolean existeDuplicada = categoriaRepository.findAll().stream()
                .anyMatch(categoria -> categoria.getNombre()
                        .equalsIgnoreCase(categoriaRequestDTO.getNombre()));

        if (existeDuplicada) {
            throw new CategoriaDuplicadaException(
                    "La categoria que se intenta agregar ya existe: " + categoriaRequestDTO.getNombre());
        }

        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaRequestDTO.getNombre());

        return categoriaRepository.save(nuevaCategoria);
    }

    @Override
    public Categoria updateCategoria(Long id, CategoriaRequestDTO categoriaRequestDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontro ninguna categoria con id " + id));

        boolean existeDuplicada = categoriaRepository.findAll().stream()
                .anyMatch(c -> !c.getId().equals(id)
                        && c.getNombre().equalsIgnoreCase(categoriaRequestDTO.getNombre()));

        if (existeDuplicada) {
            throw new CategoriaDuplicadaException(
                    "La categoria que se intenta agregar ya existe: " + categoriaRequestDTO.getNombre());
        }

        categoria.setNombre(categoriaRequestDTO.getNombre());
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontro ninguna categoria con id " + id));
        categoriaRepository.delete(categoria);
    }
}
