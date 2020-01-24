package com.easybank.repository;

import com.easybank.model.Account;
import com.easybank.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findAllByOriginEqualsOrDestinationEquals(Account origin, Account destination);

}
