package com.easybank.controller;

import com.easybank.Const;
import com.easybank.model.Agency;
import com.easybank.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Agency> save(@RequestBody Agency agency){
        agency = this.agencyRepository.save(agency);
        return new ResponseEntity<>(agency, HttpStatus.OK);
    }

    @Secured({Const.ROLE_CLIENT, Const.ROLE_MANAGER})
    @GetMapping()
    public ResponseEntity<List<Agency>> list(){
        return new ResponseEntity<>(agencyRepository.findAll(), HttpStatus.OK);
    }

}
