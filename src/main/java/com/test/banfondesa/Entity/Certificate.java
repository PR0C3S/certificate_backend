package com.test.banfondesa.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer accountNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate finishDate;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private float earnInterest;

    @Column(nullable = false)
    private float cancellInterest;

    @Column(nullable = false)
    private Boolean isAbleToCancellBefore=true;

    @Column(nullable = false)
    private Boolean isPenaltyForCancellBefore=true;

    @Column(nullable = false)
    private Boolean isAbleToPayBefore=true;

    @Column(nullable = false)
    private String status; //cancell,active,finished

    @Column(nullable = false)
    private int duration;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    @JsonIgnore
    private Client client;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
    
    public void Add(Transaction newTransaction){
        if(transactions==null){
            transactions = new ArrayList<>();
        }
        transactions.add(newTransaction);
    }
}
