package com.pedidos360.carrito_ms.service;


import com.pedidos360.carrito_ms.dto.AgregarItemRequest;
import com.pedidos360.carrito_ms.dto.ItemCarritoDTO;
import com.pedidos360.carrito_ms.exception.RecursoNoEncontradoException;
import com.pedidos360.carrito_ms.mensajeria.ClickstreamEvent;
import com.pedidos360.carrito_ms.mensajeria.ClickstreamPublisher;
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
    private final ClickstreamPublisher clickstream;

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
        ItemCarrito guardado = repository.save(item);
        clickstream.publicar(ClickstreamEvent.builder()
                .tipo("ITEM_AGREGADO")
                .usuarioId(usuarioId)
                .productoId(request.getProductoId())
                .nombreProducto(request.getNombreProducto())
                .cantidad(request.getCantidad())
                .precio(request.getPrecioUnitario())
                .origen("carrito-ms")
                .build());
        return toDTO(guardado);
    }

    @Transactional
    public void eliminar(String usuarioId, Long itemId) {
        ItemCarrito item = repository.findByIdAndUsuarioId(itemId, usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Item de carrito no encontrado: " + itemId));
        repository.deleteByIdAndUsuarioId(itemId, usuarioId);
        clickstream.publicar(ClickstreamEvent.builder()
                .tipo("ITEM_ELIMINADO")
                .usuarioId(usuarioId)
                .productoId(item.getProductoId())
                .nombreProducto(item.getNombreProducto())
                .cantidad(item.getCantidad())
                .precio(item.getPrecioUnitario())
                .origen("carrito-ms")
                .build());
    }

    /** Vacia el carrito tras una compra. NO publica ITEM_ELIMINADO: comprar no es descartar. */
    @Transactional
    public void vaciar(String usuarioId) {
        repository.deleteByUsuarioId(usuarioId);
    }

    private ItemCarritoDTO toDTO(ItemCarrito item) {
        return new ItemCarritoDTO(item.getId(), item.getProductoId(), item.getNombreProducto(),
                item.getCantidad(), item.getPrecioUnitario());
    }
}