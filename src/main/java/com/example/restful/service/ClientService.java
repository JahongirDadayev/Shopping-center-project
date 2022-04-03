package com.example.restful.service;

import com.example.restful.entity.DbClient;
import com.example.restful.payload.ClientDto;
import com.example.restful.payload.result.Result;
import com.example.restful.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    public List<DbClient> getClients() {
        return clientRepository.findAllByActive(true);
    }


    public Result postClient(ClientDto clientDto) {
        if (!clientRepository.existsByPhoneNumber(clientDto.getPhoneNumber())) {
            DbClient client = new DbClient();
            return saveClientInformation(client, clientDto, "Saved client information");
        } else {
            return new Result("The phone number you entered is busy", false);
        }
    }

    public Result updateClient(Integer id, ClientDto clientDto) {
        Optional<DbClient> optionalDbClient = clientRepository.findById(id);
        if (optionalDbClient.isPresent() && optionalDbClient.get().isActive()) {
            if (!clientRepository.existsByPhoneNumberAndIdNot(clientDto.getPhoneNumber(), id)) {
                DbClient client = optionalDbClient.get();
                return saveClientInformation(client, clientDto, "Update client information");
            }else {
                return new Result("The phone number you entered is busy", false);
            }
        } else {
            return new Result("Could not find client that matches the id you entered", false);
        }
    }

    public Result deleteClient(Integer id) {
        Optional<DbClient> optionalDbClient = clientRepository.findById(id);
        if (optionalDbClient.isPresent() && optionalDbClient.get().isActive()){
            DbClient client = optionalDbClient.get();
            client.setActive(false);
            return saveClient(client, "Delete client information");
        }else {
            return new Result("Could not find client that matches the id you entered", false);
        }
    }

    private Result saveClientInformation(DbClient client, ClientDto clientDto, String text) {
        client.setName(clientDto.getName());
        client.setPhoneNumber(clientDto.getPhoneNumber());
        return saveClient(client, text);
    }

    private Result saveClient(DbClient client, String text) {
        try {
            clientRepository.save(client);
            return new Result(text, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error dbClient table", false);
        }
    }
}
