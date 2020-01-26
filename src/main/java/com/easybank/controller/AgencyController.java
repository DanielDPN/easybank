package com.easybank.controller;

import com.easybank.model.Agency;
import com.easybank.repository.AgencyRepository;
import com.easybank.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agency")
public class AgencyController {

    private final AgencyRepository agencyRepository;

    @Autowired
    public AgencyController(AgencyRepository agencyRepository) {
        this.agencyRepository = agencyRepository;
    }

    @Secured({Const.ROLE_MANAGER})
    @PostMapping()
    public ResponseEntity save(@RequestBody Agency agency){
        try {
            agency = this.agencyRepository.save(agency);
            return ResponseEntity.status(HttpStatus.OK).body(agency);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível criar agência");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping("/{id}")
    public ResponseEntity getAgency(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(agencyRepository.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível buscar a agência");
        }
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity list(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(agencyRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possível listar agências");
        }
    }

}
