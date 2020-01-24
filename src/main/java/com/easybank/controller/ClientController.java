package com.easybank.controller;

import com.easybank.Const;
import com.easybank.model.Account;
import com.easybank.model.Client;
import com.easybank.repository.AccountRepository;
import com.easybank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @PostMapping()
    public ResponseEntity<Client> save(@RequestBody Client client){
        client = this.clientRepository.save(client);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @Secured({Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<List<Client>> list(){
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }

}
