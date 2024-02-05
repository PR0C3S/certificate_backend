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
    private LocalDate date;
    private String type; //retire,deposit
    private int amount;
}
