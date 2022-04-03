package com.example.restful.controller;

import com.example.restful.entity.DbClient;
import com.example.restful.payload.ClientDto;
import com.example.restful.payload.result.Result;
import com.example.restful.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/client")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping
    public List<DbClient> getClients(){
        return clientService.getClients();
    }

    @PostMapping
    public Result postClient(@RequestBody ClientDto clientDto){
        return clientService.postClient(clientDto);
    }

    @PutMapping(path = "/{id}")
    public Result updateClient(@PathVariable Integer id, @RequestBody ClientDto clientDto){
        return clientService.updateClient(id, clientDto);
    }

    @DeleteMapping(path = "/{id}")
    public Result deleteClient(@PathVariable Integer id){
        return clientService.deleteClient(id);
    }
}
