package com.test.banfondesa.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private String dni;
    private String fullName;
    private Date birthday;
    private String gender;
    private String location;
    private String email;
    private String phone;
}
