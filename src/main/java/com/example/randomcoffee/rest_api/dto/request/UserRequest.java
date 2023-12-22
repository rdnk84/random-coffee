package com.example.randomcoffee.rest_api.dto.request;

import com.example.randomcoffee.model.enums.AstroSign;
import com.example.randomcoffee.model.enums.Department;
import com.example.randomcoffee.model.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    String email;
    String password;
    String firstName;
    String lastName;
    String middleName;
    Gender gender;
    AstroSign astroSign;
    LocalDate hiringDate;
    Department department;

}
