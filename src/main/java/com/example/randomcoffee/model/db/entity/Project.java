package com.example.randomcoffee.model.db.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "projects")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String description;

    @ManyToMany()
    @JoinTable(
            name = "user_project"
            , joinColumns = @JoinColumn(name = "project_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<CoffeeUser> users;

}
