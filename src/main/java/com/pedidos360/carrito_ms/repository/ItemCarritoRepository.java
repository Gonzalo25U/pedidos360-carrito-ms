package com.pedidos360.carrito_ms.repository;

import com.pedidos360.carrito_ms.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    List<ItemCarrito> findByUsuarioId(String usuarioId);

    Optional<ItemCarrito> findByIdAndUsuarioId(Long id, String usuarioId);

    void deleteByIdAndUsuarioId(Long id, String usuarioId);
}
