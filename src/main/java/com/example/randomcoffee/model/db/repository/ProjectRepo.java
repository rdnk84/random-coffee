package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    @Override
    Optional<Project> findById(Long id);


    Optional<Project> findByProjectCode(String code);

    Page<Project> findByTitle(Pageable pageable, String title);

    Page<Project> findAll(Pageable pageable);
}
