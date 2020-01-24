package com.easybank.controller;

import com.easybank.Const;
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
    public ResponseEntity<Map<String, Object>> save(@RequestBody Bank bank){
        final Map<String, Object> result = new HashMap<>();
        try {
            bank = this.bankRepository.save(bank);
            result.put("success", true);
            result.put("error", null);
            result.put("body", bank);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível criar banco");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<Map<String, Object>> list(){
        final Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("error", null);
            result.put("body", bankRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível listar bancos");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
