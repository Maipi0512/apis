package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.model.Cart;

public interface CartService {

    List<Cart> getAllCarts();

    Cart getCartById(Long id);

    Cart createCart();
}
