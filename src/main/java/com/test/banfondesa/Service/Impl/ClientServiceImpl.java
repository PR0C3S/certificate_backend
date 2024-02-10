package com.test.banfondesa.Service.Impl;

import com.test.banfondesa.Entity.Client;
import java.util.List;

public interface ClientServiceImpl {
    public Client save(Client client);
    public void delete(Client client);

    public Client getById(Integer id);

    public Client getByDni(String dni);

    public List<Client> getAll();

}
