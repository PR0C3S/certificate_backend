package com.test.banfondesa.DTO;


import com.test.banfondesa.Entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {
    private Integer id;
    private Integer accountNumber;
    private LocalDate startDate;
    private LocalDate  finishDate;
    private float amount;
    private String currency;
    private String status;
    private String dni;
    private String fullName;
    private List<Transaction> transactions;

}
