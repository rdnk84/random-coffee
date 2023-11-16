package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepo extends JpaRepository<CoffeeUser, Long> {

    Optional<CoffeeUser> findByEmail(String email);

    Optional<CoffeeUser> findById(Long id);

    //    @Query("select c from CoffeeUser c where c.status <> '3' and c.lastName like %:lastName%")
    @Query(nativeQuery = true, value = "select * from users u where u.status <> 'DELETED' and u.last_name = :lastName ")
    Page<CoffeeUser> findByLastNameNotDeleted(Pageable request, @Param("lastName") String lastName);

    @Query(nativeQuery = true, value = "select * from users u where u.last_name = :lastName ")
    Page<CoffeeUser> findByLastName(Pageable request, @Param("lastName") String lastName);

    @Query(nativeQuery = true, value = "select * from users u")
    Page<CoffeeUser> findAll(Pageable request);

//    Set<CoffeeUser> findCoffeeUsersByMeetingEventId(Long id);

//    @Query(nativeQuery = true, value = "select * from users u where u.")
//   Set<CoffeeUser> findAllByEventId();

    @Query(nativeQuery = true, value =  "SELECT * FROM users u WHERE u.hiring_date >= :fromDate AND u.hiring_date <= :toDate")
    Page<CoffeeUser> findByHiringDate(Pageable request, @Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(nativeQuery = true, value = "SELECT u.* " +
            " FROM " +
            " users u," +
            " user_project up," +
            " projects p" +
            " WHERE" +
            " u.id = up.user_id" +
            " AND p.id = up.project_id" +
            " AND p.project_code = :code")
    Page<CoffeeUser> findUsersByProjectCode(Pageable request, @Param("code") String code);
}
