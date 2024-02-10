package com.test.banfondesa.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String dni;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy="client")
    private List<Certificate> certificates;
}
