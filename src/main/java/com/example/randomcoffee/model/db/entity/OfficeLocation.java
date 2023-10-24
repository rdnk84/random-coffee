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
@Table(name = "offices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfficeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String city;
    String country;

    @OneToMany
    @JsonManagedReference(value = "office_users")
    List<CoffeeUser> users;
}
