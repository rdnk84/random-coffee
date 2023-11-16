package com.example.randomcoffee.rest_api.dto.response;

import com.example.randomcoffee.model.enums.AstroSign;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse extends UserRequest {

    Long id;
    OfficeResponse office;
    Set<EventResponse> events;

    List<ProjectResponse> usersProjects;
    List<HobbyResponse> userHobbies;
}
