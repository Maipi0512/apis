package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.ProductRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Category;
import com.uade.ecom.model.Product;
import com.uade.ecom.model.Supplier;
import com.uade.ecom.repository.CategoryRepository;
import com.uade.ecom.repository.ProductRepository;
import com.uade.ecom.repository.SupplierRepository;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));
    }

    @Override
    public Product createProduct(ProductRequestDTO productRequestDTO) {
        Category category = categoryRepository.findById(productRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productRequestDTO.getCategoriaId()));

        Product product = new Product();
        product.setNombre(productRequestDTO.getNombre());
        product.setPrecio(productRequestDTO.getPrecio());
        product.setStock(productRequestDTO.getStock());
        product.setCategory(category);

        if (productRequestDTO.getProveedorId() != null) {
            Supplier supplier = supplierRepository.findById(productRequestDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontro ningun proveedor con id " + productRequestDTO.getProveedorId()));
            product.setSupplier(supplier);
        }

        return productRepository.save(product);
    }
}
