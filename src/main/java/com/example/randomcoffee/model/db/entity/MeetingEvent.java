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

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate meetingDate;

    @Enumerated(EnumType.STRING)
    EventLocation location;

    @Enumerated(EnumType.STRING)
    EventStatus status;


    @Column(name = "initiator_id")
    Long initiatorId;

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


    @ManyToMany(mappedBy = "events")
    @JsonIgnore
//    @JoinTable(
//            name = "events_users"
//            , joinColumns = @JoinColumn(name = "event_id")
//            , inverseJoinColumns = @JoinColumn(name = "user_id")
//    )
    Set<CoffeeUser> participants = new HashSet<>();
//
//    @OneToMany
//    @JsonManagedReference(value = "event_users")
//    List<CoffeeUser> participants;


//    @ManyToOne
//    @JsonBackReference(value = "events_user")
//    CoffeeUser initiator;
}
