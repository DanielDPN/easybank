package com.easybank.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private String name;
    @Column(name = "federal_registration", unique = true)
    private String federalRegistration;

    public Client() {
    }

    public Client(User user, String name, String federalRegistration) {
        super();
        this.user = user;
        this.name = name;
        this.federalRegistration = federalRegistration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
