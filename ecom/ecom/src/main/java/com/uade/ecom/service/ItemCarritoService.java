package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.model.ItemCarrito;

public interface ItemCarritoService {

    List<ItemCarrito> getAllItemsCarrito();

    ItemCarrito getItemCarritoById(Long id);

    ItemCarrito createItemCarrito(ItemCarritoRequestDTO itemCarritoRequestDTO);

    ItemCarrito updateItemCarrito(Long id, ItemCarritoRequestDTO itemCarritoRequestDTO);

    void deleteItemCarrito(Long id);
}
