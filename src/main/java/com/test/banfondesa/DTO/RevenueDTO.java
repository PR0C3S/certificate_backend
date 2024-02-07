package com.test.banfondesa.DTO;

import com.test.banfondesa.Entity.Transaction;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueDTO {

    private String currency;
    private Integer id;
    private LocalDate startDate;
    private LocalDate  finishDate;
    private String status;
    private String dni;
    private String fullName;
    private float amount;
    private float expectedAmount;

    private List<Transaction> transactions = new ArrayList<>();
}
