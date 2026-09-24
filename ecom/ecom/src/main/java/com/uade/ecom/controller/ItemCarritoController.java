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

import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.service.ItemCarritoService;

@RestController
@RequestMapping("/itemsCarrito")
public class ItemCarritoController {

    @Autowired
    private ItemCarritoService itemCarritoService;

    @GetMapping
    public List<ItemCarrito> getAllItemsCarrito() {
        return itemCarritoService.getAllItemsCarrito();
    }

    @GetMapping("/{id}")
    public ItemCarrito getItemCarritoById(@PathVariable Long id) {
        return itemCarritoService.getItemCarritoById(id);
    }

    @PostMapping
    public ItemCarrito createItemCarrito(@RequestBody ItemCarritoRequestDTO itemCarritoRequestDTO) {
        return itemCarritoService.createItemCarrito(itemCarritoRequestDTO);
    }

    @PutMapping("/{id}")
    public ItemCarrito updateItemCarrito(@PathVariable Long id, @RequestBody ItemCarritoRequestDTO itemCarritoRequestDTO) {
        return itemCarritoService.updateItemCarrito(id, itemCarritoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemCarrito(@PathVariable Long id) {
        itemCarritoService.deleteItemCarrito(id);
    }
}
