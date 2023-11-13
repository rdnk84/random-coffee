package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.EventRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.EventService;
import com.example.randomcoffee.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
    private final UserRepo userRepo;
    private final ObjectMapper mapper;


    @Override
    public EventResponse getEventDto(Long id) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        Set<CoffeeUser> participants = event.getParticipants();
        Long initiatorId = event.getInitiatorId();
        CoffeeUser user = userRepo.findById(initiatorId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        EventResponse result = mapper.convertValue(event, EventResponse.class);
        Set<UserResponse> participantList = participants.stream()
                .map(p -> mapper.convertValue(p, UserResponse.class))
                .collect(Collectors.toSet());
        ;
        result.setParticipants(participantList);
        result.setInitiatorLastName(user.getLastName());
        return result;
    }

    @Override
    public EventResponse updateEvent(Long id, EventRequest request) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!event.getStatus().equals(EventStatus.CANCELLED)) {
            event.setTitle(StringUtils.isBlank(request.getTitle()) ? event.getTitle() : request.getTitle());
            event.setEventTheme(request.getEventTheme() == null ? event.getEventTheme() : request.getEventTheme());
            event.setMeetingDate(request.getMeetingDate() == null ? event.getMeetingDate() : request.getMeetingDate());
            event.setMeetingTime(request.getMeetingTime() == null ? event.getMeetingTime() : request.getMeetingTime());
            event.setLocation(request.getLocation() == null ? event.getLocation() : request.getLocation());
            event.setUpdatedAt(LocalDateTime.now());

            MeetingEvent save = eventRepo.save(event);
            return mapper.convertValue(save, EventResponse.class);
        }
        throw new CustomException("This meeting was already cancelled", HttpStatus.NOT_FOUND);
    }


    @Override
    public EventResponse createEvent(Long initiatorId, EventRequest request) {

        if (StringUtils.isBlank(request.getTitle()) || request.getLocation() == null || request.getMeetingDate() == null) {
            throw new CustomException("Some of highlighted fields can not be blank", HttpStatus.BAD_REQUEST);
        }
        MeetingEvent event = mapper.convertValue(request, MeetingEvent.class);
        event.setStatus(EventStatus.INITIATED);
        event.setCreatedAt(LocalDateTime.now());
        CoffeeUser initiator = userRepo.findById(initiatorId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        event.setInitiatorId(initiatorId);

        Set<CoffeeUser> participants = event.getParticipants();
        participants.add(initiator);
        Set<MeetingEvent> events = initiator.getEvents();
        events.add(event);
        initiator.setEvents(events);
        event.setParticipants(participants);
        userRepo.save(initiator);
        MeetingEvent save = eventRepo.save(event);
        EventResponse response = mapper.convertValue(save, EventResponse.class);
        response.setInitiatorLastName(initiator.getFirstName());
        Set<UserResponse> participantsList = participants.stream()
                .map(p -> mapper.convertValue(p, UserResponse.class))
                .collect(Collectors.toSet());
        response.setParticipants(participantsList);
        return response;
    }

    @Override
    public void deleteEvent(Long id) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!event.getStatus().equals(EventStatus.CANCELLED)) {
            event.setStatus(EventStatus.CANCELLED);
            event.setUpdatedAt(LocalDateTime.now());
            eventRepo.save(event);
            return;
        }
        throw new CustomException("This meeting was already cancelled", HttpStatus.NOT_FOUND);
    }

    @Override
    public EventResponse ifEventAlreadyExist(String day, String time, EventLocation location) {
        Time sqlTime = Time.valueOf(time);
        Date sqlDay = Date.valueOf(day);
        MeetingEvent byDateAndTime = eventRepo.findByDateAndLocation(sqlDay, sqlTime, location).orElseThrow(() -> new CustomException("not found", HttpStatus.NOT_FOUND));
        EventResponse result = mapper.convertValue(byDateAndTime, EventResponse.class);
        return result;
    }


    public void sendEvent(Long eventId, Long userid) {
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        String userNotFound = String.format("User with id %d not found", userid);
        CoffeeUser user = userRepo.findById(userid).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        if (event.getInitiatorId() != userid) {
            Set<CoffeeUser> participants = event.getParticipants();
            participants.add(user);
            event.setParticipants(participants);
            event.setUpdatedAt(LocalDateTime.now());

            Set<MeetingEvent> userEvents = user.getEvents();
            userEvents.add(event);
            user.setEvents(userEvents);
            user.setUpdatedAt(LocalDateTime.now());
            eventRepo.save(event);
            userRepo.save(user);
            return;
        }
        throw new CustomException("This User is initiator of the Event", HttpStatus.NOT_FOUND);
    }

    @Override
    public Set<CoffeeUser> checkAllEventParticipants(Long eventId) {
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        Set<CoffeeUser> participants = event.getParticipants();
        return participants;
    }

    @Override
    public void userDeclineEvent(Long eventId, Long userId) {
        String userNotFound = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        Long initiatorId = event.getInitiatorId();
        Set<CoffeeUser> participants = event.getParticipants();
        Set<MeetingEvent> userEvents = user.getEvents();
        if (initiatorId != userId) {
            for (MeetingEvent theEvent : userEvents) {
                Long searchEventId = theEvent.getId();
                if (searchEventId == eventId) {
                    userEvents.remove(event);
                    participants.remove(user);
                    event.setParticipants(participants);
                    user.setEvents(userEvents);
                    event.setUpdatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());
                    eventRepo.save(event);
                    userRepo.save(user);
                    return;
                }
            }
            throw new CustomException("The user has not have such event", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("This user is initiator of the event, please use method deleteEvent(eventId)", HttpStatus.NOT_FOUND);
    }

    @Override
    public Page<EventResponse> getEventsInPeriod(Integer page, Integer perPage, String sort, Sort.Direction order, String fromDay, String toDay) {
        Date sqlFromDay = Date.valueOf(fromDay);
        Date sqlToDay = Date.valueOf(toDay);
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<MeetingEvent> events = eventRepo.findByDaysBetween(pageRequest, sqlFromDay, sqlToDay);
        List<EventResponse> eventsByDate = events.getContent().stream()
                .map(e -> mapper.convertValue(e, EventResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(eventsByDate);
    }

    @Override
    public Page<EventResponse> getEventsInDayTimePeriod(Integer page, Integer perPage, String sort, Sort.Direction order, String day, String fromTime, String toTime) {
        Time sqlFromTime = Time.valueOf(fromTime);
        Time sqlToTime = Time.valueOf(toTime);
        Date sqlDay = Date.valueOf(day);
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<MeetingEvent> events = eventRepo.findEventsByTimePeriod(pageRequest, sqlDay, sqlFromTime, sqlToTime);
        List<EventResponse> eventsByDate = events.getContent().stream()
                .map(e -> mapper.convertValue(e, EventResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(eventsByDate);
    }

    @Override
    public Page<EventResponse> allEventsByDay(Integer page, Integer perPage, String sort, Sort.Direction order, String day) {
        java.sql.Date sqlDay = java.sql.Date.valueOf(day);
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<MeetingEvent> events = eventRepo.eventsByDay(pageRequest, sqlDay);
        List<EventResponse> eventsByDate = events.getContent().stream()
                .map(e -> mapper.convertValue(e, EventResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(eventsByDate);
    }

}
