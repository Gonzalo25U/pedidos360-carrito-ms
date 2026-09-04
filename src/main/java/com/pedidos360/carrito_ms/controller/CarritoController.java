package com.pedidos360.carrito_ms.controller;

import com.pedidos360.carrito_ms.dto.AgregarItemRequest;
import com.pedidos360.carrito_ms.dto.ItemCarritoDTO;
import com.pedidos360.carrito_ms.security.UsuarioActualProvider;
import com.pedidos360.carrito_ms.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService service;
    private final UsuarioActualProvider usuarioActualProvider;

    @GetMapping
    public ResponseEntity<List<ItemCarritoDTO>> listar() {
        return ResponseEntity.ok(service.listar(usuarioActualProvider.obtenerUsuarioId()));
    }

    @PostMapping
    public ResponseEntity<ItemCarritoDTO> agregar(@Valid @RequestBody AgregarItemRequest request) {
        ItemCarritoDTO creado = service.agregar(usuarioActualProvider.obtenerUsuarioId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long itemId) {
        service.eliminar(usuarioActualProvider.obtenerUsuarioId(), itemId);
        return ResponseEntity.noContent().build();
    }
}