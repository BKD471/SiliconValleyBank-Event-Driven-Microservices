package com.siliconvalley.cardservices.repository;

import com.siliconvalley.cardservices.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardsRepository extends JpaRepository<Cards,String> {
    Optional<List<Cards>> findAllByCustomerId(String customerId);
    Optional<Cards> findByCardNumber(String cardNumber);
}
