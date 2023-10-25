package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficeRepo extends JpaRepository<Office, Long> {

    @Override
    Optional<Office> findById(Long aLong);
}
