package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.CartItemRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Cart;
import com.uade.ecom.model.CartItem;
import com.uade.ecom.model.Product;
import com.uade.ecom.repository.CartItemRepository;
import com.uade.ecom.repository.CartRepository;
import com.uade.ecom.repository.ProductRepository;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
    }

    @Override
    public CartItem createCartItem(CartItemRequestDTO cartItemRequestDTO) {
        Cart cart = cartRepository.findById(cartItemRequestDTO.getCarritoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun carrito con id " + cartItemRequestDTO.getCarritoId()));

        Product product = productRepository.findById(cartItemRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + cartItemRequestDTO.getProductoId()));

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setCantidad(cartItemRequestDTO.getCantidad());

        return cartItemRepository.save(cartItem);
    }
}
