package com.example.randomcoffee.rest_api.dto.request;

import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventTheme;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequest {
    String title;
    EventTheme eventTheme;

//    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd HH:mm:ss")
//    LocalDateTime meetingDateTime;

    //    Date meetingDate;

    LocalDate meetingDate;
    Time meetingTime;
    EventLocation location;
//    Set<Object> participants = new HashSet<>();
}
