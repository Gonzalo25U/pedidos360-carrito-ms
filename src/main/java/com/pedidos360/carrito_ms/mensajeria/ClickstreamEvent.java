package com.pedidos360.carrito_ms.mensajeria;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickstreamEvent {
    private String eventId;
    private String tipo;
    private String usuarioId;
    private Long productoId;
    private String nombreProducto;
    private String categoria;
    private Integer cantidad;
    private BigDecimal precio;
    private String origen;
    /** ISO-8601 (UTC), como String para no depender de modulos extra de Jackson. */
    private String timestamp;
}