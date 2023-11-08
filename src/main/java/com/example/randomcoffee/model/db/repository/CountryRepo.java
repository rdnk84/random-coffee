package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.enums.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CountryRepo extends JpaRepository<Country, Long> {

    Optional<Country> findById(Long id);

//    @Query(nativeQuery = true, value = "select * from countries c where c.title = :title ")
    Optional<Country> findByTitle(String title);
    @Override
    List<Country> findAll();

    List<Country> findByRegion(Region region);


}
