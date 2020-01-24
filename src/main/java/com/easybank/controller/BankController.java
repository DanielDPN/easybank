package com.easybank.controller;

import com.easybank.Const;
import com.easybank.model.Bank;
import com.easybank.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Bank> save(@RequestBody Bank bank){
        bank = this.bankRepository.save(bank);
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @Secured({Const.ROLE_MANAGER})
    @PutMapping()
    public ResponseEntity<Bank> edit(@RequestBody Bank bank){
        bank = this.bankRepository.save(bank);
        return new ResponseEntity<>(bank, HttpStatus.OK);
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<List<Bank>> list(){
        return new ResponseEntity<>(bankRepository.findAll(), HttpStatus.OK);
    }

}
