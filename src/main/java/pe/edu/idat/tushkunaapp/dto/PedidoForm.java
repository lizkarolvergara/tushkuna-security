package pe.edu.idat.tushkunaapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PedidoForm {

    @NotNull(message = "Selecciona una mesa")
    @Min(value = 1, message = "Mesa inválida")
    @Max(value = 8, message = "Mesa inválida")
    private Integer mesaNumero;

    @NotBlank(message = "La descripción es requerida")
    @Size(min = 3, max = 200, message = "Descripción debe tener entre 3 y 200 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ,.-]+$",
            message = "Descripción contiene caracteres no permitidos")
    private String descripcion;

    @NotNull(message = "El total es requerido")
    @DecimalMin(value = "0.1", message = "El total debe ser mayor a 0")
    @DecimalMax(value = "9999.99", message = "El total no puede exceder 9999.99")
    private java.math.BigDecimal total;
}