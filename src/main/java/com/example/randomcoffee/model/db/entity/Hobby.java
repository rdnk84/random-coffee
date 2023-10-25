package com.example.randomcoffee.model.db.entity;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "хобби")
public class Hobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String description;

    @ManyToMany()
    @JoinTable(
            name = "hobby_user"
            , joinColumns = @JoinColumn(name = "hobby_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<CoffeeUser> users;
}
