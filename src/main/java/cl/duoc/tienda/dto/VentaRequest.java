package cl.duoc.tienda.dto;
import jakarta.validation.constraints.*;
public record VentaRequest(@NotNull @Positive Long productoId,
                           @Min(1) int cantidad) { }
