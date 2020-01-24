package com.easybank.model;

import com.easybank.enums.MovementType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private MovementType type;
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "origin_id")
    private Account origin;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "destination_id")
    private Account destination;

    public Movement() {
    }

    public Movement(MovementType type, BigDecimal amount, Account origin, Account destination) {
        super();
        this.type = type;
        this.amount = amount;
        this.origin = origin;
        this.destination = destination;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getOrigin() {
        return origin;
    }

    public void setOrigin(Account origin) {
        this.origin = origin;
    }

    public Account getDestination() {
        return destination;
    }

    public void setDestination(Account destination) {
        this.destination = destination;
    }

}
