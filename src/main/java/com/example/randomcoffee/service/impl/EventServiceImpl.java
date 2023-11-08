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
    public EventResponse getEvent(Long id) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        EventResponse result = mapper.convertValue(event, EventResponse.class);
        return result;
    }

    @Override
    public Page<EventResponse> eventsByDate(Integer page, Integer perPage, String sort, Sort.Direction order, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<MeetingEvent> events = eventRepo.findByDateBetween(pageRequest, startDate, endDate);
        List<EventResponse> eventsByDate = events.getContent().stream()
                .map(e -> mapper.convertValue(e, EventResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(eventsByDate);
    }

    @Override
    public EventResponse updateEvent(Long id, EventRequest request) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!event.getStatus().equals(EventStatus.CANCELLED)) {
            event.setTitle(StringUtils.isBlank(request.getTitle()) ? event.getTitle() : request.getTitle());
            event.setEventTheme(request.getEventTheme() == null ? event.getEventTheme() : request.getEventTheme());
            event.setMeetingDate(request.getMeetingDate() == null ? event.getMeetingDate() : request.getMeetingDate());
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
        MeetingEvent save = eventRepo.save(event);
        EventResponse response = mapper.convertValue(save, EventResponse.class);
        response.setInitiatorFirstName(initiator.getFirstName());
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
    public Boolean ifEventAlreadyExist(LocalDateTime startDate, EventLocation location) {
        MeetingEvent byDateAndTime = eventRepo.findByDateAndLocation(startDate, location).orElseThrow(() -> new CustomException("not found", HttpStatus.NOT_FOUND));
        Long eventId = byDateAndTime.getId();
        String errorMsg = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (byDateAndTime != null && !event.getStatus().equals(EventStatus.CANCELLED)) {
            return true;
        }
        return false;
    }

    public void sendEvent(Long userid, Long eventId) {
        String userNotFound = String.format("User with id %d not found", userid);
        CoffeeUser user = userRepo.findById(userid).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        if (event.getInitiatorId() != userid) {
            Set<CoffeeUser> participants = event.getParticipants();
            participants.add(user);
            event.setUpdatedAt(LocalDateTime.now());
            event.setParticipants(participants);
//            SortedSet<CoffeeUser> sortedSet = new TreeSet<>();
//            Iterator<CoffeeUser> iterator = sortedSet.iterator();
//            Iterable<CoffeeUser> iterable = (Iterable<CoffeeUser>) sortedSet;
//            eventRepo.saveAllAndFlush(iterable);
            return;
        }
        throw new CustomException("This User is initiator of the Event", HttpStatus.NOT_FOUND);
    }

    public Set<CoffeeUser> checkAllParticipants(Long eventId) {
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        Set<CoffeeUser> participants = event.getParticipants();
        Integer size = participants.size();
        return participants;
    }

}
