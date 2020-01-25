package com.easybank.controller;

import com.easybank.util.Const;
import com.easybank.model.Agency;
import com.easybank.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> save(@RequestBody Agency agency){
        final Map<String, Object> result = new HashMap<>();
        try {
            agency = this.agencyRepository.save(agency);
            result.put("success", true);
            result.put("error", null);
            result.put("body", agency);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível criar agência");
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
            result.put("body", agencyRepository.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Não foi possível listar agências");
            result.put("body", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

}
