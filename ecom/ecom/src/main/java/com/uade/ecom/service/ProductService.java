package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.ProductRequestDTO;
import com.uade.ecom.model.Product;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(ProductRequestDTO productRequestDTO);
}
