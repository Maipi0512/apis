package com.uade.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CarritoResponseDTO;
import com.uade.ecom.dto.ItemCarritoCantidadDTO;
import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.service.CarritoService;
import com.uade.ecom.service.ItemCarritoService;

/**
 * Items del carrito del usuario autenticado. La linea se identifica por
 * el productoId y, si el producto tiene variantes (color/numero), el
 * varianteId (como en Mercado Libre, cada producto+variante aparece una
 * sola vez), no por un id propio. Cada operacion devuelve el carrito
 * entero actualizado, con el total nuevo.
 *
 * Para productos con variantes: PUT/DELETE /carritos/items/{productoId}?varianteId=3
 */
@RestController
@RequestMapping("/carritos/items")
public class ItemCarritoController {

    @Autowired
    private ItemCarritoService itemCarritoService;

    @Autowired
    private CarritoService carritoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarritoResponseDTO agregar(@RequestBody ItemCarritoRequestDTO itemCarritoRequestDTO) {
        itemCarritoService.agregar(itemCarritoRequestDTO);
        return miCarrito();
    }

    @PutMapping("/{productoId}")
    public CarritoResponseDTO cambiarCantidad(@PathVariable Long productoId,
            @RequestParam(required = false) Long varianteId,
            @RequestBody ItemCarritoCantidadDTO itemCarritoCantidadDTO) {
        itemCarritoService.cambiarCantidad(productoId, varianteId, itemCarritoCantidadDTO.getCantidad());
        return miCarrito();
    }

    @DeleteMapping("/{productoId}")
    public CarritoResponseDTO quitar(@PathVariable Long productoId,
            @RequestParam(required = false) Long varianteId) {
        itemCarritoService.quitar(productoId, varianteId);
        return miCarrito();
    }

    private CarritoResponseDTO miCarrito() {
        return CarritoResponseDTO.from(carritoService.getItemsDeMiCarrito());
    }
}
