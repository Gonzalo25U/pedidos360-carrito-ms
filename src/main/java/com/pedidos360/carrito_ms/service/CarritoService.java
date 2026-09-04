package com.pedidos360.carrito_ms.service;

import com.pedidos360.carrito_ms.dto.AgregarItemRequest;
import com.pedidos360.carrito_ms.dto.ItemCarritoDTO;
import com.pedidos360.carrito_ms.exception.RecursoNoEncontradoException;
import com.pedidos360.carrito_ms.model.ItemCarrito;
import com.pedidos360.carrito_ms.repository.ItemCarritoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final ItemCarritoRepository repository;

    public List<ItemCarritoDTO> listar(String usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream().map(this::toDTO).toList();
    }

    public ItemCarritoDTO agregar(String usuarioId, AgregarItemRequest request) {
        ItemCarrito item = new ItemCarrito(
                null,
                usuarioId,
                request.getProductoId(),
                request.getNombreProducto(),
                request.getCantidad(),
                request.getPrecioUnitario(),
                null
        );
        return toDTO(repository.save(item));
    }

    @Transactional
    public void eliminar(String usuarioId, Long itemId) {
        repository.findByIdAndUsuarioId(itemId, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Item de carrito no encontrado: " + itemId));
        repository.deleteByIdAndUsuarioId(itemId, usuarioId);
    }

    private ItemCarritoDTO toDTO(ItemCarrito item) {
        return new ItemCarritoDTO(item.getId(), item.getProductoId(), item.getNombreProducto(),
                item.getCantidad(), item.getPrecioUnitario());
    }
}
