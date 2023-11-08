package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.service.impl.HobbyServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/hobby")
@RequiredArgsConstructor
public class HobbyRestController {

    private final HobbyServiceImpl hobbyService;

    @PostMapping("/")
    @Operation(summary = "Заведение нового хобби")
    public HobbyResponse createHobby(@RequestBody HobbyRequest request) {

        return hobbyService.createHobby(request);
    }

    @Operation(summary = "Найти хобби по id")
    @GetMapping("/{id}")
    public HobbyResponse getHobby(@PathVariable Long id) {
        return hobbyService.getHobbyDto(id);
    }

    @Operation(summary = "Удалить хобби")
    @DeleteMapping("/{id}")
    public void DeleteHobby(@PathVariable Long id) {

         hobbyService.deleteHobby(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Внести изменения в существующее хобби")
    public HobbyResponse updateHobby(@PathVariable Long id, @RequestBody HobbyRequest request) {

        return hobbyService.updateHobby(id, request);
    }

    @Operation(summary = "Все хобби")
    @GetMapping("/all-hobbies")
    public Page<HobbyResponse> getAllHobbies(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer perPage,
                                            @RequestParam(defaultValue = "title") String sort,
                                            @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        return hobbyService.getAllHobbies(page, perPage, sort, order);
    }

    @Operation(summary = "Все хобби по указанному названию")
    @GetMapping("/by-title")
    public Page<HobbyResponse> getByTitle(@RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer perPage,
                                             @RequestParam(defaultValue = "title") String sort,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                             @RequestParam(required = false) String title) {
        return hobbyService.getHobbyByTitle(page, perPage, sort, order, title);
    }

}
