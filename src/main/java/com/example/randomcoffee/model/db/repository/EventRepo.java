package com.example.randomcoffee.model.db.repository;

import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.enums.EventLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepo extends JpaRepository<MeetingEvent, Long> {

    Optional<MeetingEvent> findById(Long id);

//    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE ")
//    Set<MeetingEvent> findAllByUserId(@Param("userId") List userId);

    //    @Query(nativeQuery = true, value = "SELECT * FROM events e WHERE e.meeting_date > 'pubDateStart' AND e.meeting_date < NOW()")
//    List<MeetingEvent> findByDate(@Param("currDate") LocalDateTime currDate, @Param("dateEnd") Date dateEnd);

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.meeting_date  BETWEEN :dayStart AND :dayEnd")
    Page<MeetingEvent> findByDaysBetween(Pageable request, @Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.meeting_date = :day AND e.meeting_time  BETWEEN :timeStart AND :timeEnd")
    Page<MeetingEvent> findEventsByTimePeriod(Pageable request,  @Param("day") Date day, @Param("timeStart") Time timeStart, @Param("timeEnd") Time timeEnd);

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.meeting_date = :day")
    Page<MeetingEvent> eventsByDay(Pageable request, @Param("day") Date day);

//    Page<MeetingEvent> findByParticipant();

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.meeting_date = :day AND e.meeting_time = :time AND e.location = :#{#location.toString()}")
    Optional<MeetingEvent> findByDateAndLocation(@Param("day") Date day, @Param("time") Time time, @Param("location") EventLocation location);

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.status <> 'CANCELLED'")
    List<MeetingEvent> findNotCancelled(Long id);

    @Query(nativeQuery = true, value = "SELECT * FROM event e WHERE e.status <> 'CANCELLED'")
    List<MeetingEvent> findAllNotCancelled();

//    Set<MeetingEvent> findAllByUserId(Long id);


}
