package com.test.banfondesa.Service;

import com.test.banfondesa.Entity.Client;
import com.test.banfondesa.Repository.ClientRepository;
import com.test.banfondesa.Service.Impl.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService implements ClientServiceImpl {

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Override
    public Client getById(Integer id) {
        return clientRepository.findById(id).orElse(null);
    }


    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }
}
