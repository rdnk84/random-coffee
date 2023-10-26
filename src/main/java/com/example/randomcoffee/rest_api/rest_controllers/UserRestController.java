package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpl userService;


    @Operation(summary = "Create user")
    @PostMapping("/")
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Get CoffeeUser by id")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserDto(id);
    }

    @Operation(summary = "Get CoffeeUsers by LastName")
    @GetMapping("/lastName")
    public Page<UserResponse> usersByLastName(@RequestParam(defaultValue = "1") Integer page,
                                                  @RequestParam(defaultValue = "10") Integer perPage,
                                                  @RequestParam(defaultValue = "email") String sort,
                                                  @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                  @RequestParam(required = false) String lastName) {
        return userService.usersByLastName(page, perPage, sort, order, lastName);
    }

    @Operation(summary = "Delete CoffeeUser by id")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
         userService.deleteUser(id);
    }

    @Operation(summary = "Update CoffeeUser")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }
}
