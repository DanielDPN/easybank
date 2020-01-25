package com.easybank.controller;

import com.easybank.util.Const;
import com.easybank.model.Bank;
import com.easybank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class BankController {

    private final BankRepository bankRepository;

    @Autowired
    public BankController(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @PostMapping()
    public ResponseEntity save(@RequestBody Bank bank){
        try {
            bank = this.bankRepository.save(bank);
            return ResponseEntity.status(HttpStatus.OK).body(bank);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar banco");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity list(){
        final Map<String, Object> result = new HashMap<>();
        try {
            return ResponseEntity.status(HttpStatus.OK).body(bankRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível listar bancos");
        }
    }

}
