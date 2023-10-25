package com.example.randomcoffee.model.db.entity;

import com.example.randomcoffee.model.enums.Countries;
import com.example.randomcoffee.model.enums.OfficeStatus;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "offices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "city")
    String city;

    @Enumerated(EnumType.STRING)
    Countries country;

    @Column(name = "created_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    OfficeStatus status;

    @OneToMany
    @JsonManagedReference(value = "office_users")
    List<CoffeeUser> users;

    @ManyToMany()
    @JoinTable(
            name = "office_department"
            , joinColumns = @JoinColumn(name = "office_id")
            , inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    List<Department> offices;

}
