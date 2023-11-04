package com.example.randomcoffee.rest_api.rest_controllers;


import com.example.randomcoffee.rest_api.dto.request.EventRequest;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.service.impl.EventServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/meeting-event")
@RequiredArgsConstructor
public class EventRestController {

    private final EventServiceImpl service;

    @Operation(summary = "Get Event by id")
    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id) {
        return service.getEvent(id);
    }

    @Operation(summary = "Create new event")
    @PostMapping("/")
    public EventResponse createEvent(@RequestParam("userId") Long userId, @RequestBody EventRequest request) {
        return service.createEvent(userId, request);
    }

    @Operation(summary = "Delete Event by id")
    @DeleteMapping("/{id}")
    public void deleteEvent(Long id) {
        service.deleteEvent(id);
    }

    @Operation(summary = "Update Event")
    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        return service.updateEvent(id, request);
    }

    @Operation(summary = "Send invitation of event to user")
    @PostMapping("/sendInvitation/{userUser}/{eventId}")
    public EventResponse sendInvitationToUser(@PathVariable Long userId, @PathVariable Long eventId) {
        return service.sendEvent(userId, eventId);
    }

}
