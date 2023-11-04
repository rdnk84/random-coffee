package com.example.randomcoffee.model.db.entity;

import com.example.randomcoffee.model.enums.EventAcceptane;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.model.enums.EventTheme;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@Builder
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "детали встречи")
public class MeetingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Enumerated(EnumType.STRING)
    EventTheme eventTheme;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate meetingDate;

    @Enumerated(EnumType.STRING)
    EventLocation location;

    @Enumerated(EnumType.STRING)
    EventStatus status;

//    @Enumerated(EnumType.STRING)
//    EventAcceptane acceptance;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedAt;

    @ManyToOne
    @JsonBackReference(value = "events_user")
    CoffeeUser initiator;

    @OneToMany
    @JsonManagedReference(value = "event_users")
    List<CoffeeUser> participants;
}
