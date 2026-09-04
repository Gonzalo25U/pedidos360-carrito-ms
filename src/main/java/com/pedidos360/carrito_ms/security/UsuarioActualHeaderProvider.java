package com.pedidos360.carrito_ms.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


@Component
@Profile("local")
@RequestScope
public class UsuarioActualHeaderProvider implements UsuarioActualProvider {

    private final HttpServletRequest request;

    public UsuarioActualHeaderProvider(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String obtenerUsuarioId() {
        String header = request.getHeader("X-User-Id");
        return (header != null && !header.isBlank()) ? header : "usuario-anonimo-local";
    }
}
