package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Cart;
import com.uade.ecom.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun carrito con id " + id));
    }

    @Override
    public Cart createCart() {
        return cartRepository.save(new Cart());
    }
}
