package com.example.randomcoffee.model.db.entity;


import com.example.randomcoffee.model.enums.AstroSign;
import com.example.randomcoffee.model.enums.Gender;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "сотрудник")
public class CoffeeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty
    String email;

    @NotEmpty
    String password;

    @NotEmpty
    @Column(name = "first_name")
    String firstName;

    @NotEmpty
    @Column(name = "last_name")
    String lastName;

    @Column(name = "work_period")
    Integer periodOfWork;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(name = "image_url")
    private String image;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    UserActivityStatus status;

    Integer meetingsCount;

    @Enumerated(EnumType.STRING)
    AstroSign astroSign;

    @ManyToOne
    @JsonBackReference(value = "hobby_users")
    Hobby hobby;

    @ManyToOne
    @JsonBackReference(value = "department_users")
    Department department;

    @ManyToOne
    @JsonBackReference(value = "office_users")
    OfficeLocation officeLocation;

    @OneToMany
    @JsonManagedReference(value = "user_projects")
    List<Project> projects;
}

