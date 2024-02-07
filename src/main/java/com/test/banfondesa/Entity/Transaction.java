package com.test.banfondesa.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    private String type; //deposit-retire
    private String message;
    private float amount;

    @ManyToOne
    @JoinColumn(name="certificate_id", nullable=false)
    @JsonIgnore
    private Certificate certificate;
}
