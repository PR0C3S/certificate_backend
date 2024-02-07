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
public class CertificateDTO {

    @NotNull(message = "Fecha de inicio es requerido")
    private LocalDate startDate;
    private int duration;
    private Integer idClient;
    private float amount;

    @NotEmpty(message = "Moneda es requerido")
    private String currency;
    private float earnInterest=0.10f;
    private float cancellInterest=0.25f;
    private Boolean isAbleToCancellBefore=false;
    private Boolean isPenaltyForCancellBefore=true;
    private Boolean isAbleToPayBefore=false;

    @NotEmpty(message = "Status es requerido")
    private String status;

}
