package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.EventRepo;
import com.example.randomcoffee.model.db.repository.OfficeRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.model.enums.EventStatus;
import com.example.randomcoffee.model.enums.EventTheme;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class EventServiceImplTest {

    @InjectMocks
    EventServiceImpl eventService;

    @Mock
    EventRepo eventRepo;

    @Spy
    ObjectMapper mapper;

    @Mock
    UserRepo userRepo;


    @Test
    void getEventDto() {
        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        event.setInitiatorId(2L);
        CoffeeUser initiator = new CoffeeUser();
        initiator.setId(2L);
        when(userRepo.findById(initiator.getId())).thenReturn(Optional.of(initiator));
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
        EventResponse result = eventService.getEventDto(event.getId());
        assertEquals(Optional.of(result.getId()), Optional.of(event.getId()));
    }

    @Test
    void updateEvent() {

        EventRequest request = new EventRequest();
        request.setTitle("another event");

        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        event.setEventTheme(EventTheme.COFFEE);
        event.setStatus(EventStatus.INITIATED);
        event.setLocation(EventLocation.INSIDE);
        event.setTitle("some event");

        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
        when(eventRepo.save(any(MeetingEvent.class))).thenReturn(event);
        EventResponse result = eventService.updateEvent(event.getId(), request);
        assertEquals(result.getTitle(), request.getTitle());
        assertEquals(result.getLocation(), event.getLocation());
    }

//    @Test
//    void createEvent() {
//
//        EventRequest request = new EventRequest();
//        request.setEventTheme(EventTheme.COFFEE);
//        request.setLocation(EventLocation.INSIDE);
//        request.setTitle("some event");
////        request.setMeetingDate();
//
//        MeetingEvent event = new MeetingEvent();
//        event.setId(1L);
//        event.setInitiatorId(2L);
//        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
//        when(eventRepo.save(any(MeetingEvent.class))).thenReturn(event);
//        EventResponse result = eventService.createEvent(2L, request);
//        assertEquals(result.getTitle(), request.getTitle());
//    }

    @Test
    void deleteEvent() {
        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        event.setStatus(EventStatus.INITIATED);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
        eventService.deleteEvent(event.getId());
        verify(eventRepo, times(1)).save(any(MeetingEvent.class));
        assertEquals(EventStatus.CANCELLED, event.getStatus());
    }

//    @Test
//    void ifEventAlreadyExist() {
//        String day = "2018-07-28";
//        String time = "11:00:00";
//        MeetingEvent event = new MeetingEvent();
//        event.setId(1L);
//        event.setLocation(EventLocation.INSIDE);
////        when(eventRepo.findByDateAndLocation(day, time, event.getLocation())).thenReturn(Optional.of(event));
//        eventService.ifEventAlreadyExist(day, time, event.getLocation());
//    }

    @Test
    void sendEvent() {
        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        event.setInitiatorId(2L);

        CoffeeUser user = new CoffeeUser();
        user.setId(3L);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        Set<CoffeeUser> participants = event.getParticipants();
        participants.add(user);
        event.setParticipants(participants);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));

        Set<MeetingEvent> events = new HashSet<>();
        events.add(event);
        user.setEvents(events);

        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);
        when(eventRepo.save(any(MeetingEvent.class))).thenReturn(event);
        eventService.sendEvent(1L, 3L);
        assertEquals(participants.size(), 1);
        assertEquals(events.size(), 1);
    }

    @Test
    void checkAllEventParticipants() {
        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        CoffeeUser user = new CoffeeUser();
        user.setId(3L);
        Set<CoffeeUser> participants = (Set.of(user));
        event.setParticipants(participants);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
        eventService.checkAllEventParticipants(event.getId());
        assertEquals(participants.size(), 1);
    }

    @Test
    void userDeclineEvent() {
        MeetingEvent event = new MeetingEvent();
        event.setId(1L);
        event.setInitiatorId(2L);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));

        CoffeeUser user = new CoffeeUser();
        user.setId(3L);

//        Set<CoffeeUser> participants = (Set.of(user));
        Set<CoffeeUser> participants = new HashSet<>();
        participants.add(user);
        event.setParticipants(participants);

//        Set<MeetingEvent> events = (Set.of(event));
        Set<MeetingEvent> events = new HashSet<>();
        events.add(event);
        user.setEvents(events);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);
        when(eventRepo.save(any(MeetingEvent.class))).thenReturn(event);

        eventService.userDeclineEvent(1L, 3L);
        assertEquals(participants.size(), 0);
        assertEquals(events.size(), 0);
    }

    @Test
    void getEventsInPeriod() {
    }

    @Test
    void getEventsInDayTimePeriod() {
    }

    @Test
    void allEventsByDay() {
    }
}