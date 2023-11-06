package com.example.randomcoffee.rest_api.rest_controllers;


import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/meeting-event")
@RequiredArgsConstructor
public class EventRestController {

    private final EventServiceImpl service;

    @Operation(summary = "Получить мероприятие по его идент.номеру")
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return service.getEvent(id);
    }

    @Operation(summary = "Создать новое мероприятие")
    @PostMapping("/{initiatorId}/")
    public EventResponse createEvent(@PathVariable Long initiatorId, @RequestBody EventRequest request) {
        return service.createEvent(initiatorId, request);
    }

    @Operation(summary = "Удалить мероприятие")
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
    @PutMapping("/sendInvitation/{userId}/{eventId}")
    public void sendInvitationToUser(@PathVariable Long userId, @PathVariable Long eventId) {
        service.sendEvent(userId, eventId);
    }

    @Operation(summary = "Все участники данного мероприятия")
    @GetMapping("/{eventId}/all-participants")
    public Set<CoffeeUser> allParticipants(@PathVariable Long eventId) {
        return service.checkAllParticipants(eventId);
    }

}
