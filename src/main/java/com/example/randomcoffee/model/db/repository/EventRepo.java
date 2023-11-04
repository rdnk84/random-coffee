package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.enums.EventLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface EventRepo extends JpaRepository<MeetingEvent, Long> {

    Optional<MeetingEvent> findById(Long id);

    //    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.meeting_date > 'pubDateStart' AND e.meeting_date < NOW()")
//    List<MeetingEvent> findByDate(@Param("currDate") LocalDateTime currDate, @Param("dateEnd") Date dateEnd);

    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.meeting_date BETWEEN :dateStart AND :dateEnd")
    Page<MeetingEvent> findByDateBetween(Pageable request, @Param("dateStart") LocalDateTime dateStart, @Param("dateEnd") LocalDateTime dateEnd);

//    Page<MeetingEvent> findByParticipant();

    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.meeting_date = :dateStart AND e.location = :location")
    Optional<MeetingEvent> findByDateAndLocation(@Param("dateStart") LocalDateTime dateStart, @Param("location")EventLocation location);

    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.status <> 'CANCELLED'")
    Optional<MeetingEvent> findNotCancelled(Long id);
    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.status <> 'CANCELLED'")
    List<MeetingEvent> findAllNotCancelled();
}
