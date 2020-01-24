package com.easybank.controller;

import com.easybank.Const;
import com.easybank.model.Client;
import com.easybank.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> save(@RequestBody Client client){
        final Map<String, Object> result = new HashMap<>();
        try {
            client = this.clientRepository.save(client);
            result.put("success", true);
            result.put("error", null);
            result.put("body", client);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível criar cliente");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<Map<String, Object>> list(){
        final Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("error", null);
            result.put("body", clientRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível listar clientes");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
