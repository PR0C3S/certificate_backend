package com.test.banfondesa.DTO;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDTO {
    private Date startDate;
    private Date finishDate;
    private String amount;
    private String currency;
    private float earnInterest;
    private float cancellInterest;
    private Boolean isAbleToCancellBefore;
    private Boolean isPenaltyForCancellBefore;
    private Boolean isAbleToPayBefore;
    private String status; //desactive,active,delete

}
