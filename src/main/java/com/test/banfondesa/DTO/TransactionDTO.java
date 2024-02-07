package com.test.banfondesa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Date date;
    private String type; //retire,deposit
    private String message;
    private int amount;
    private Integer certificateId;
}
