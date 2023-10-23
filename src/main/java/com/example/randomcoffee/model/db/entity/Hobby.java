package com.example.randomcoffee.model.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@Table(name = "hobbies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hobby {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
