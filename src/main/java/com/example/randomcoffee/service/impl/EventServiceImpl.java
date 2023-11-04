package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.EventRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.EventAcceptane;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
        MeetingEvent event = eventRepo.findNotCancelled(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        event.setTitle(StringUtils.isBlank(request.getTitle()) ? event.getTitle() : request.getTitle());

        event.setEventTheme(request.getEventTheme() == null ? event.getEventTheme() : request.getEventTheme());
        event.setMeetingDate(request.getMeetingDate() == null ? event.getMeetingDate() : request.getMeetingDate());
        event.setLocation(request.getLocation() == null ? event.getLocation() : request.getLocation());
        event.setUpdatedAt(LocalDateTime.now());
        MeetingEvent save = eventRepo.save(event);
        return mapper.convertValue(save, EventResponse.class);
    }


    @Override
    public EventResponse createEvent(Long userId, EventRequest request) {

        if (StringUtils.isBlank(request.getTitle()) || request.getLocation() == null || request.getMeetingDate() == null) {
            throw new CustomException("Some of highlighted fields can not be blank", HttpStatus.BAD_REQUEST);
        }
        MeetingEvent event = mapper.convertValue(request, MeetingEvent.class);
        event.setStatus(EventStatus.INITIATED);
        event.setCreatedAt(LocalDateTime.now());
        CoffeeUser initiator = userRepo.findById(userId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        event.setInitiator(initiator);
        List<CoffeeUser> participants = event.getParticipants();
        participants.add(initiator);

        MeetingEvent save = eventRepo.save(event);
        EventResponse response = mapper.convertValue(save, EventResponse.class);
        return response;
    }

    @Override
    public void deleteEvent(Long id) {
        String errorMsg = String.format("Event with id %d not found", id);
        MeetingEvent event = eventRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        MeetingEvent notCancelled = eventRepo.findNotCancelled(id).orElseThrow(() -> new CustomException("not found", HttpStatus.NOT_FOUND));
        if (notCancelled != null) {
            event.setStatus(EventStatus.CANCELLED);
            event.setUpdatedAt(LocalDateTime.now());
            eventRepo.save(event);
        }
    }

    @Override
    public Boolean ifEventAlreadyExist(LocalDateTime startDate, EventLocation location) {
        MeetingEvent byDateAndTime = eventRepo.findByDateAndLocation(startDate, location).orElseThrow(() -> new CustomException("not found", HttpStatus.NOT_FOUND));
        Long eventId = byDateAndTime.getId();
        MeetingEvent notCancelled = eventRepo.findNotCancelled(eventId).orElseThrow(() -> new CustomException("not found", HttpStatus.NOT_FOUND));
        if (byDateAndTime != null && notCancelled != null) {
            return true;
        }
        return false;
    }

    public EventResponse sendEvent(Long userid, Long eventId) {
        String userNotFound = String.format("User with id %d not found", userid);
        CoffeeUser user = userRepo.findById(userid).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        List<CoffeeUser> participants = event.getParticipants();
        participants.add(user);

        event.setUpdatedAt(LocalDateTime.now());
        MeetingEvent save = eventRepo.save(event);
        EventResponse result = mapper.convertValue(save, EventResponse.class);
        return result;
    }


//    public void eventAccept(Long eventId, Long userid, EventAcceptane acceptance) {
//        String eventNotFound = String.format("Event with id %d not found", eventId);
//        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
//        event.setAcceptane(acceptance);
//        if(event.getAcceptane().equals(EventAcceptane.DECLINE)){
//            String userNotFound = String.format("User with id %d not found", userid);
//            CoffeeUser user = userRepo.findById(userid).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
//            List<CoffeeUser> users = event.getUsers();
//            users.remove(user);
//            event.setPeopleCount(users.size());
//        }
//        event.setUpdatedAt(LocalDateTime.now());
//    }

}
