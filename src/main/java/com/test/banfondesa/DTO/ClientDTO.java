package com.test.banfondesa.DTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {

    @NotEmpty(message = "Cedula es requerido")
    private String dni;
    @NotEmpty(message = "Nombre es requerido")
    private String fullName;
    @NotNull(message = "Fecha de nacimiento es requerido")
    private LocalDate birthday;

    @NotEmpty(message = "Genero es requerido")
    private String gender;

    @NotEmpty(message = "Ubicacion es requerido")
    private String location;

    @NotEmpty(message = "Correo es requerido")
    private String email;

    @NotEmpty(message = "Telefono es requerido")
    private String phone;
}
