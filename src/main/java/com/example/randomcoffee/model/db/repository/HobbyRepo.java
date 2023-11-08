package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.Hobby;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HobbyRepo extends JpaRepository<Hobby, Long> {


    Optional<Hobby> findById(Long id);


    @Query("select h from Hobby h where h.status <> 'DELETED' and  h.title like %:title%")
    Page<Hobby> findByTitle(Pageable request, @Param("title") String title);


}
