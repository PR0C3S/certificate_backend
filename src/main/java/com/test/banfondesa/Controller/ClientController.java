package com.test.banfondesa.Controller;


import com.test.banfondesa.DTO.ClientDTO;
import com.test.banfondesa.Entity.Client;
import com.test.banfondesa.Exception.ResourceNotFoundException;
import com.test.banfondesa.Service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(value ="http://localhost:5173")
public class ClientController
{
    @Autowired
    private ClientService clientService;


    @GetMapping("/list")
    public List<Client> listClientGET(){
        return clientService.getAll();
    }

    @GetMapping("/update/{id}")
    public Client updateClientGET(@PathVariable Integer id){
        Client client = clientService.getById(id);
        if(client == null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }
        return  client;
    }

    @PostMapping("/created")
    public Client createClientPOST(@Valid @RequestBody ClientDTO newClient){

        //Validate dni, email y telefono
        Client client = new Client(null, newClient.getDni(), newClient.getFullName(), newClient.getBirthday(),
                newClient.getGender(), newClient.getLocation(), newClient.getEmail(), newClient.getPhone(),null);
        return clientService.save(client);
    }

    @GetMapping("/findByDni/{id}")
    public Client findByDniGet(@PathVariable String id){
      return  clientService.getByDni(id);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Client> updateClientPATCH(@PathVariable Integer id, @Valid @RequestBody ClientDTO updatedClient){
        Client client = clientService.getById(id);
        if(client == null){
            throw new ResourceNotFoundException("No se pudo encontrar el cliente con el id: "+id);
        }
        //Validate dni, email y telefono
        client.setDni(updatedClient.getDni());
        client.setFullName(updatedClient.getFullName());
        client.setBirthday(updatedClient.getBirthday());
        client.setGender(updatedClient.getGender());
        client.setLocation(updatedClient.getLocation());
        client.setEmail(client.getEmail());
        client.setPhone(updatedClient.getPhone());
        clientService.save(client);
        return ResponseEntity.ok(client);
    }

}
