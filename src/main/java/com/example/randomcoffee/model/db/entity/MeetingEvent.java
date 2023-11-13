package com.example.randomcoffee.model.db.entity;

import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.model.enums.EventTheme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@Builder
@Table(name = "event")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "детали встречи")
public class MeetingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Enumerated(EnumType.STRING)
    EventTheme eventTheme;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
////    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    LocalDateTime meetingDateTime;

//    @Basic
//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    Date meetingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate meetingDate;

    @Basic
    @Temporal(TemporalType.TIME)
//   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Time meetingTime;

    @Enumerated(EnumType.STRING)
    EventLocation location;

    @Enumerated(EnumType.STRING)
    EventStatus status;

    @Column(name = "initiator_id")
    Long initiatorId;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedAt;


    @ManyToMany(mappedBy = "events")
    @JsonIgnore
    Set<CoffeeUser> participants = new HashSet<>();

}
