package com.test.banfondesa.DTO;

import com.test.banfondesa.Entity.Certificate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueWithCertificate {
    List<RevenueDTO> revenueDTOList;
    private String currency;
    private Integer idCertificate;
    private String statusCertificate;
    private Integer idClient;
    private String nameCliente;
}
