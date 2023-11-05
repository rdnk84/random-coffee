package com.example.randomcoffee.rest_api.dto.response;

import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse extends EventRequest {

    Long id;
    EventStatus status;
    String initiatorFirstName;
    Long initiatorId;
}
