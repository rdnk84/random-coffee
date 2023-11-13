package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface EventService {

    EventResponse getEventDto(Long id);

//    Page<EventResponse> eventsBetweenDates (Integer page, Integer perPage, String sort, Sort.Direction order, String fromDay, String toDay);

    EventResponse updateEvent(Long id, EventRequest request);

    EventResponse createEvent(Long initiatorId, EventRequest request);

    void deleteEvent(Long id);

    EventResponse ifEventAlreadyExist(String day, String time, EventLocation location);

//    EventResponse ifEventAlreadyExist(String day, String time);
    void userDeclineEvent(Long eventId, Long userId);
    Set<CoffeeUser> checkAllEventParticipants(Long eventId);

    Page<EventResponse> getEventsInPeriod(Integer page, Integer perPage, String sort, Sort.Direction order, String fromDay, String toDay);

    Page<EventResponse> getEventsInDayTimePeriod(Integer page,
                                       Integer perPage,
                                       String sort, Sort.Direction order,
                                       String day,
                                       String fromTime,
                                       String toTime);

    Page<EventResponse> allEventsByDay(Integer page, Integer perPage, String sort, Sort.Direction order, String day);
}
