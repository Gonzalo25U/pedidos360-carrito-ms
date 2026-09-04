package com.pedidos360.carrito_ms.security;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


@Component
@Profile("!local")
public class UsuarioActualJwtProvider implements UsuarioActualProvider {

    @Override
    public String obtenerUsuarioId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject();
    }
}
