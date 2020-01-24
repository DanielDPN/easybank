package com.easybank.controller;

import com.easybank.Const;
import com.easybank.model.Account;
import com.easybank.model.Deposit;
import com.easybank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @PostMapping()
    public ResponseEntity<Account> save(@RequestBody Account account) {
        account = this.accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<List<Account>> list() {
        return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping()
    public ResponseEntity<Account> deposit(@RequestBody Deposit deposit) {
        Account account = deposit.getAccount();
        account.setBalance(account.getBalance().add(deposit.getAmount()));
        account = accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

}
