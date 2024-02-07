package com.test.banfondesa.DTO;


import com.test.banfondesa.Entity.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private float balance;
    private Integer idCertificado;
    private Integer numeroCuenta;
    private Date startDate;
    private Date finishDate;
    private float amount;
    private String status;
    private String dni;
    private String nameClient;

}
