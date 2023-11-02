package com.example.randomcoffee.rest_api.rest_controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/meeting-event")
@RequiredArgsConstructor
public class EventRestController {


}
