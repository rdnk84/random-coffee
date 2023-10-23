package com.example.randomcoffee.rest_api;

import com.example.randomcoffee.rest_api.dto.UserRequest;
import com.example.randomcoffee.rest_api.dto.UserResponse;
import com.example.randomcoffee.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

private final UserServiceImpl userService;

    @PostMapping("/")
    @Operation(summary = "Create user")
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Get CoffeeUser by id")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserDto(id);
    }

}
