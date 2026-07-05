package pe.edu.idat.tushkunaapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {

    @NotBlank(message = "Usuario requerido")
    @Size(min = 3, max = 50, message = "Usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Usuario solo puede contener letras, números y guion bajo")
    private String username;

    @NotBlank(message = "Contraseña requerida")
    @Size(min = 6, max = 50, message = "Contraseña debe tener entre 6 y 50 caracteres")
    private String password;
}