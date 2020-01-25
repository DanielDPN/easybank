package com.easybank.controller;

import com.easybank.enums.MovementType;
import com.easybank.model.*;
import com.easybank.repository.AccountRepository;
import com.easybank.repository.MovementRepository;
import com.easybank.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public ResponseEntity save(@RequestBody Account account) {
        try {
            account = this.accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar conta");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity list() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(accountRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível listar contas");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/deposit")
    public ResponseEntity deposit(@RequestBody Deposit deposit) {
        try {
            Account account = deposit.getAccount();
            account.setBalance(account.getBalance().add(deposit.getAmount()));
            account = accountRepository.save(account);
            movementRepository.save(new Movement(MovementType.DEPOSIT, deposit.getAmount(), null, account));
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível efetuar depósito");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/withdraw")
    public ResponseEntity withdraw(@RequestBody Withdraw withdraw) {
        try {
            Account account = accountRepository.getOne(withdraw.getAccount().getId());
            if (account.getBalance().compareTo(BigDecimal.ZERO) <= 0 || account.getBalance().compareTo(withdraw.getAmount()) < 0) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Saldo insuficiente para saque");
            }
            movementRepository.save(new Movement(MovementType.WITHDRAW, withdraw.getAmount(), account, null));
            return ResponseEntity.status(HttpStatus.OK).body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível efetuar saque");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @PutMapping("/transfer")
    public ResponseEntity transfer(@RequestBody Transfer transfer) {
        try {
            Account destination = transfer.getDestination();
            Account origin = accountRepository.getOne(transfer.getOrigin().getId());
            if (origin.getBalance().compareTo(BigDecimal.ZERO) <= 0 || origin.getBalance().compareTo(transfer.getAmount()) < 0) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Saldo insuficiente para transferência");
            }
            origin.setBalance(origin.getBalance().subtract(transfer.getAmount()));
            destination.setBalance(destination.getBalance().add(transfer.getAmount()));
            accountRepository.save(destination);
            accountRepository.save(origin);
            movementRepository.save(new Movement(MovementType.TRANSFER, transfer.getAmount(), origin, destination));
            return ResponseEntity.status(HttpStatus.OK).body(destination);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível efetuar transferência");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping("/extract")
    public ResponseEntity extract(@RequestParam Long id) {
        try {
            Account account = accountRepository.getOne(id);
            return ResponseEntity.status(HttpStatus.OK).body(movementRepository.findAllByOriginEqualsOrDestinationEquals(account, account));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível buscar extrato");
        }
    }

}
