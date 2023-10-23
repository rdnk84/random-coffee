package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<CoffeeUser, Long> {

    Optional<CoffeeUser> findByEmail(String email);
    Optional<CoffeeUser> findById(Long id);
}
