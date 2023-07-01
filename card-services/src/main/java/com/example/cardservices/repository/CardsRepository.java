package com.example.cardservices.repository;

import com.example.cardservices.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardsRepository extends JpaRepository<Cards,Long> {
    Optional<List<Cards>> findAllByCustomerId(Long id);


}
