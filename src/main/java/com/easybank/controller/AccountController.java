package com.easybank.controller;

import com.easybank.util.Const;
import com.easybank.enums.MovementType;
import com.easybank.model.*;
import com.easybank.repository.AccountRepository;
import com.easybank.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @PostMapping()
    public ResponseEntity<Map<String, Object>> save(@RequestBody Account account) {
        final Map<String, Object> result = new HashMap<>();
        try {
            account = this.accountRepository.save(account);
            result.put("success", true);
            result.put("error", null);
            result.put("body", account);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível criar conta");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<Map<String, Object>> list() {
        final Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("error", null);
            result.put("body", accountRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível listar contas");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/deposit")
    public ResponseEntity<Map<String, Object>> deposit(@RequestBody Deposit deposit) {
        Account account;
        final Map<String, Object> result = new HashMap<>();
        try {
            account = deposit.getAccount();
            account.setBalance(account.getBalance().add(deposit.getAmount()));
            account = accountRepository.save(account);
            movementRepository.save(new Movement(MovementType.DEPOSIT, deposit.getAmount(), null, account));
            result.put("success", true);
            result.put("error", null);
            result.put("body", account);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível efetuar depósito");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/withdraw")
    public ResponseEntity<Map<String, Object>> withdraw(@RequestBody Withdraw withdraw) {
        Account account;
        final Map<String, Object> result = new HashMap<>();
        try {
            account = accountRepository.getOne(withdraw.getAccount().getId());
            if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0) {
                result.put("success", false);
                result.put("error", "Saldo insuficiente para saque");
                result.put("body", null);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
            }
            movementRepository.save(new Movement(MovementType.WITHDRAW, withdraw.getAmount(), account, null));
            result.put("success", true);
            result.put("error", null);
            result.put("body", account);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível efetuar saque");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/transfer")
    public ResponseEntity<Map<String, Object>> transfer(@RequestBody Transfer transfer) {
        Account destination = transfer.getDestination();
        final Map<String, Object> result = new HashMap<>();
        try {
            Account origin = accountRepository.getOne(transfer.getOrigin().getId());
            if (origin.getBalance().compareTo(BigDecimal.ZERO) <= 0
                    || origin.getBalance().compareTo(transfer.getAmount()) < 0) {
                result.put("success", false);
                result.put("error", "Saldo insuficiente para transferência");
                result.put("body", null);
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
            }
            origin.setBalance(origin.getBalance().subtract(transfer.getAmount()));
            destination.setBalance(destination.getBalance().add(transfer.getAmount()));
            accountRepository.save(destination);
            accountRepository.save(origin);
            movementRepository.save(new Movement(MovementType.TRANSFER, transfer.getAmount(), origin, destination));
            result.put("success", true);
            result.put("error", null);
            result.put("body", destination);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível efetuar transferência");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping("/extract")
    public ResponseEntity<Map<String, Object>> extract(@RequestParam Long id) {
        final Map<String, Object> result = new HashMap<>();
        try {
            Account account = accountRepository.getOne(id);
            result.put("success", true);
            result.put("error", null);
            result.put("body", movementRepository.findAllByOriginEqualsOrDestinationEquals(account, account));
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível buscar extrato");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
