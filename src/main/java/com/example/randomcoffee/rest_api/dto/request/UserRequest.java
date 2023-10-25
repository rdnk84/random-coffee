package com.example.randomcoffee.rest_api.dto.request;

import com.example.randomcoffee.model.enums.AstroSign;
import com.example.randomcoffee.model.enums.Gender;
import com.example.randomcoffee.model.enums.Hobby;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

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
    Hobby hobby;

//    String cityLocation;
//    Date startWorkDate;
//    String hobby;
//    String project;
//    String department;
}
