package com.easybank.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(name = "federal_registration", unique = true)
    private String federalRegistration;

    public Client() {
    }

    public Client(String name, String federalRegistration) {
        super();
        this.name = name;
        this.federalRegistration = federalRegistration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFederalRegistration() {
        return federalRegistration;
    }

    public void setFederalRegistration(String federalRegistration) {
        this.federalRegistration = federalRegistration;
    }

}
