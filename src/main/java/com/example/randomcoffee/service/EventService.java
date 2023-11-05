package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface EventService {

    EventResponse getEvent(Long id);

    Page<EventResponse> eventsByDate (Integer page, Integer perPage, String sort, Sort.Direction order, LocalDateTime startDate, LocalDateTime endDate);

    EventResponse updateEvent(Long id, EventRequest request);

    EventResponse createEvent(Long initiatorId, EventRequest request);

    void deleteEvent(Long id);

    Boolean ifEventAlreadyExist(LocalDateTime meetingDate, EventLocation location);
}
