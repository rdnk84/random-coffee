package com.example.randomcoffee.model.db.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name="детали встречи")
public class MeetingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


}
