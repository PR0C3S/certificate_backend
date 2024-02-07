package com.test.banfondesa.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountNumber;
    private Date startDate;
    private Date finishDate;
    private float amount;
    private String currency;
    private float earnInterest;
    private float cancellInterest;
    private Boolean isAbleToCancellBefore;
    private Boolean isPenaltyForCancellBefore;
    private Boolean isAbleToPayBefore;
    private String status; //cancell,active,finished
    private int duration;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    @JsonIgnore
    private Client client;


    @OneToMany(mappedBy="certificate")
    private List<Transaction> transactions;

    public void Addtransaction(Transaction newTransaction){
        transactions.add(newTransaction);
    }


    public float getCurrentAmount(int months){
        return amount*earnInterest*months;
    }


    public float getCurrentCancelInterest(int months){
        return amount*cancellInterest*months;
    }
}
