package com.test.banfondesa.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String dni;
    private String fullName;
    private Date birthday;
    private String gender;
    private String location;
    private String email;
    private String phone;

    @OneToMany(mappedBy="client")
    private List<Certificate> certificates;
}
