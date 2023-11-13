package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Пользователи")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpl userService;


    @Operation(summary = "Создать пользователя")
    @PostMapping("/")
    public UserResponse createUser(@RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @Operation(summary = "Получить пользователя по id")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUserDto(id);
    }

    @Operation(summary = "Получить пользователей по фамилии")
    @GetMapping("/lastName")
    public Page<UserResponse> usersByLastName(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer perPage,
                                              @RequestParam(defaultValue = "email") String sort,
                                              @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                              @RequestParam(required = false) String lastName) {
        return userService.usersByLastName(page, perPage, sort, order, lastName);
    }

    @Operation(summary = "Получить всех пользователей")
    @GetMapping("/all")
    public Page<UserResponse> getAllUsers(@RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer perPage,
                                          @RequestParam(defaultValue = "email") String sort,
                                          @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        return userService.allUsers(page, perPage, sort, order);
    }

    @Operation(summary = "Удалить пользователя по id")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @Operation(summary = "Изменить данные пользователя")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @Operation(summary = "Все мероприятия пользователя")
    @GetMapping("/{userId}/all-events")
    public Set<MeetingEvent> getAllEvents(@PathVariable Long userId) {
        return userService.checkAllUserEvents(userId);
    }


    @Operation(summary = "Перемещение пользователя в другой офис")
    @PutMapping("/office-change/{userId}/{officeId}")
    public UserResponse changeOffice(@PathVariable Long userId, @PathVariable Long officeId) {
        return userService.userChangeOffice(userId, officeId);
    }

    @Operation(summary = "Получить всех пользователей по выбранному периоду поступления на работу")
    @GetMapping("/hiring-date")
    public Page<UserResponse> allUsersByHiringDate(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer perPage,
                                                   @RequestParam(defaultValue = "email") String sort,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                   @RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate) {
        return userService.findByHiringPeriod(page, perPage, sort, order, fromDate, toDate);
    }

}
