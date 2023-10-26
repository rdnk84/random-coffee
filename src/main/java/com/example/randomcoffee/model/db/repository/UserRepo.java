package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<CoffeeUser, Long> {

    Optional<CoffeeUser> findByEmail(String email);

    Optional<CoffeeUser> findById(Long id);

    //    @Query("select c from CoffeeUser c where c.status <> '3' and c.lastName like %:lastName%")
    @Query(nativeQuery = true, value = "select * from users u where u.status <> 'DELETED' and u.last_name = :lastName")
    Page<CoffeeUser> findByLastNameNotDeleted(Pageable request, @Param("lastName") String lastName);


}
