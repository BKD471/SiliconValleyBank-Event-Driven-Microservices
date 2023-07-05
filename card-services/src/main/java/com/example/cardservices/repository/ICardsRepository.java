package com.example.cardservices.repository;

import com.example.cardservices.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardsRepository extends JpaRepository<Cards,String> {
    Optional<List<Cards>> findAllByCustomerId(String customerId);
    boolean findByCustomerIdExists(String customerId);
    Optional<Cards> findByCardNumber(String cardNumber);
}
