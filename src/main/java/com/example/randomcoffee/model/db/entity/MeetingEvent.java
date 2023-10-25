package com.example.randomcoffee.model.db.entity;

import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.model.enums.EventTheme;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "детали встречи")
public class MeetingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    Integer peopleCount;

    @Enumerated(EnumType.STRING)
    EventTheme eventTheme;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime meetingDate;

    @Enumerated(EnumType.STRING)
    EventStatus eventStatus;

    @Enumerated(EnumType.STRING)
    EventLocation location;

    @OneToMany
    @JsonManagedReference(value = "event_users")
    List<CoffeeUser> users;
}
