package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.EventRepo;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepo eventRepo;
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
        event.setPeopleCount(request.getPeopleCount() == null ? event.getPeopleCount() : request.getPeopleCount());
        event.setEventTheme(request.getTheme() == null ? event.getEventTheme() : request.getTheme());
        event.setMeetingDate(request.getLocalDateTime() == null ? event.getMeetingDate() : request.getLocalDateTime());
        event.setLocation(request.getLocation() == null ? event.getLocation() : request.getLocation());
        event.setUpdatedAt(LocalDateTime.now());
        MeetingEvent save = eventRepo.save(event);
        return mapper.convertValue(save, EventResponse.class);
    }


    @Override
    public EventResponse createEvent(EventRequest request) {

        if (ifEventAlreadyExist(request.getLocalDateTime(), request.getLocation())) {
            throw new CustomException("event already exists", HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(request.getTitle()) || request.getLocation() == null || request.getLocalDateTime() == null) {
            throw new CustomException("Some of highlighted fields can not be blank", HttpStatus.BAD_REQUEST);
        }
        MeetingEvent event = mapper.convertValue(request, MeetingEvent.class);
        event.setStatus(EventStatus.INITIATED);
        event.setCreatedAt(LocalDateTime.now());
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
}
