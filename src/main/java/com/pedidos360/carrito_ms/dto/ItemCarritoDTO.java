package com.pedidos360.carrito_ms.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
