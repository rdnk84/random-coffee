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
@Table(name = "departments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "title")
    String title;

    String description;

    @OneToMany
    @JsonManagedReference(value = "department_users")
    List<CoffeeUser> users;

    @ManyToMany()
    @JoinTable(
            name = "office_department"
            , joinColumns = @JoinColumn(name = "department_id")
            , inverseJoinColumns = @JoinColumn(name = "office_id")
    )
    List<Office> offices;
}
