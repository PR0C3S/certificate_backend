package com.test.banfondesa.DTO;


import com.test.banfondesa.Entity.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private float balance;
    private Integer idCertificado;
    private String statusCertificado;
    private Integer idCliente;
    private String nombreCliente;

}
