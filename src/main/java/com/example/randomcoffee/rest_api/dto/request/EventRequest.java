package com.example.randomcoffee.rest_api.dto.request;

import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventTheme;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequest {
    String title;
    Integer peopleCount;
    EventTheme theme;
    LocalDateTime localDateTime;
    EventLocation location;

}
