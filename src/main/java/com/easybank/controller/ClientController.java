package com.easybank.controller;

import com.easybank.model.Client;
import com.easybank.repository.ClientRepository;
import com.easybank.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity list(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(clientRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível listar clientes");
        }
    }

}
