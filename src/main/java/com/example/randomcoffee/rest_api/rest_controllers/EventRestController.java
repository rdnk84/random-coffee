package com.example.randomcoffee.rest_api.rest_controllers;


import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.enums.EventLocation;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Мероприятия")
@RestController
@RequestMapping("/api/meeting-event")
@RequiredArgsConstructor
public class EventRestController {

    private final EventServiceImpl service;

    @Operation(summary = "Получить мероприятие по его id")
    @GetMapping("/{id}")
    public EventResponse getEventDto(@PathVariable Long id) {

        return service.getEventDto(id);
    }

    @Operation(summary = "Создать новое мероприятие")
    @PostMapping("/{initiatorId}/")
    public EventResponse createEvent(@PathVariable Long initiatorId, @RequestBody EventRequest request) {
        return service.createEvent(initiatorId, request);
    }

    @Operation(summary = "Удалить мероприятие по id")
    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
    }

    @Operation(summary = "Внести изменения в мероприятие")
    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        return service.updateEvent(id, request);
    }

    @Operation(summary = "Отправка мероприятия другому пользователю")
    @PutMapping("/send-invitation/{eventId}/{userId}")
    public void sendInvitationToUser(@PathVariable Long eventId, @PathVariable Long userId) {
        service.sendEvent(eventId, userId);
    }

    @Operation(summary = "Отклонение пользователем мероприятия")
    @PutMapping("/decline/{eventId}/{userId}")
    public void declineEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        service.userDeclineEvent(eventId, userId);
    }

    @Operation(summary = "Все участники данного мероприятия")
    @GetMapping("/{eventId}/all-participants")
    public Set<CoffeeUser> allParticipants(@PathVariable Long eventId) {
        return service.checkAllEventParticipants(eventId);
    }

    @Operation(summary = "Все мероприятия в рамках выбранного периода дней")
    @GetMapping("/in-period")
    public Page<EventResponse> eventsInPeriod(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "event_theme") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam("fromDay") String fromDay,
                                              @RequestParam("toDay") String toDay) {
        return service.getEventsInPeriod(page, perPage, sort, order, fromDay, toDay);
    }

    @Operation(summary = "Все мероприятия дня в заданный промежуток времени")
    @GetMapping("/in-day-time-period")
    public Page<EventResponse> eventsInDayTimePeriod(@RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer perPage,
                                                     @RequestParam(defaultValue = "event_theme") String sort,
                                                     @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                     @RequestParam("day") String day,
                                                     @RequestParam("fromTime") String fromTime,
                                                     @RequestParam("toTime") String toTime) {
        return service.getEventsInDayTimePeriod(page, perPage, sort, order, day, fromTime, toTime);
    }

    @Operation(summary = "Все мероприятия дня")
    @GetMapping("/by-day")
    public Page<EventResponse> eventsByDay(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer perPage,
                                           @RequestParam(defaultValue = "event_theme") String sort,
                                           @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                           @RequestParam("day") String day) {
        return service.allEventsByDay(page, perPage, sort, order, day);
    }

    @Operation(summary = "Проверка есть ли уже такое мероприятие по дате, времени и локации")
    @GetMapping("/event-exists-already")
    public EventResponse ifEventExists(@RequestParam("day") String day,
                                       @RequestParam("time") String startTime,
                                       @RequestParam("location") EventLocation location) {
        return service.ifEventAlreadyExist(day, startTime, location);
    }

//    @Operation(summary = "Проверка есть ли уже такое мероприятие по дате, времени и локации")
//    @GetMapping("/event-exists-already")
//    public EventResponse ifEventExists(@RequestParam("day") String day,
//                                       @RequestParam("time") String startTime) {
//        return service.ifEventAlreadyExist(day, startTime);
//    }

}
