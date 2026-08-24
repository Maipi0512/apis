package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CartItemRequestDTO;
import com.uade.ecom.model.CartItem;
import com.uade.ecom.service.CartItemService;

@RestController
@RequestMapping("/items-carrito")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping
    public List<CartItem> getAllCartItems() {
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/{id}")
    public CartItem getCartItemById(@PathVariable Long id) {
        return cartItemService.getCartItemById(id);
    }

    @PostMapping
    public CartItem createCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        return cartItemService.createCartItem(cartItemRequestDTO);
    }
}
