package com.uade.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.ItemCarrito;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByCarritoId(Long carritoId);

    // Lista y no Optional: carritos armados antes de que la linea se
    // identificara por producto pueden tener el mismo producto repetido.
    // El filtro por variante se hace en ItemCarritoServiceImpl (la variante
    // puede ser null).
    List<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

    List<ItemCarrito> findByVariante_Id(Long varianteId);

    List<ItemCarrito> findByProductoId(Long productoId);
}
