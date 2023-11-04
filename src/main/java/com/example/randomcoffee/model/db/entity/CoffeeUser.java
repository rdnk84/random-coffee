package com.example.randomcoffee.model.db.entity;


import com.example.randomcoffee.model.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
//@Builder
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag(name = "сотрудник")
//public class CoffeeUser implements UserDetails {
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

    @Column(name = "middle_name")
    String MiddleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate hiringDate;
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


    @Enumerated(EnumType.STRING)
    AstroSign astroSign;

    @Enumerated(EnumType.STRING)
    Department department;

    @Enumerated(EnumType.STRING)
    Role role;

    @ManyToMany()
    @JoinTable(
            name = "user_hobby"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "hobby_id")
    )
    List<Hobby> hobbies;

    @ManyToOne
    @JsonBackReference(value = "office_users")
    Office office;

    @ManyToMany()
    @JoinTable(
            name = "user_project"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    List<Project> projects;

//    @ManyToOne
//    @JsonBackReference(value = "event_users")
//    MeetingEvent event;

    @OneToMany
    @JsonManagedReference(value = "events_user")
    List<MeetingEvent> events;

    @ManyToOne
    @JsonBackReference(value = "event_users")
    MeetingEvent event;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}

