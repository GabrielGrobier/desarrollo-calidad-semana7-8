package cl.duoc.tienda.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** DTO de entrada. @Valid activa estas reglas en el controlador HTTP. */
public record ProductoRequest(
        @NotBlank String sku,
        @NotBlank String nombre,
        @NotBlank String categoria,
        @NotNull @DecimalMin("0.01") BigDecimal precio,
        @Min(0) int stock) { }
