package com.example.randomcoffee.model.db.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "hobbies")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Hobby {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String hobbyTitle;

    String description;

    @OneToMany
    @JsonManagedReference(value = "hobby_users")
    List<CoffeeUser> users;
}
