package com.example.randomcoffee.model.db.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "countries")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "страна")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String country;

    @OneToMany
    @JsonManagedReference(value = "office_country")
    List<Office> users;

}
