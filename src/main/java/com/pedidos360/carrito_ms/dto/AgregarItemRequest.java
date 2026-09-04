package com.pedidos360.carrito_ms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgregarItemRequest {

    @NotNull(message = "productoId es obligatorio")
    private Long productoId;

    @NotBlank(message = "nombreProducto es obligatorio")
    private String nombreProducto;

    @NotNull(message = "cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "precioUnitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precioUnitario;
}
