package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.CartItemRequestDTO;
import com.uade.ecom.model.CartItem;

public interface CartItemService {

    List<CartItem> getAllCartItems();

    CartItem getCartItemById(Long id);

    CartItem createCartItem(CartItemRequestDTO cartItemRequestDTO);
}
