package com.test.banfondesa.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuracion {
    @Id
    private String name;
    private float certificateInterest;
    private float penaltyForCancellCertificate;
    private int minToOpenCertificate;
    private int maxToOpenCertificate;
}
