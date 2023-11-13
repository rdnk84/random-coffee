package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.impl.OfficeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Офисы")
@RestController
@RequestMapping("/api/office")
@RequiredArgsConstructor
public class OfficeRestController {

    private final OfficeServiceImpl officeService;

    @PostMapping("/")
    @Operation(summary = "Заведение нового офиса")
    public OfficeResponse createOffice(@RequestBody OfficeRequest request) {

        return officeService.createOffice(request);
    }

    @Operation(summary = "Найти офис по id")
    @GetMapping("/{id}")
    public OfficeResponse getOffice(@PathVariable Long id) {
        return officeService.getOfficeById(id);
    }

    @Operation(summary = "Удалить офис по id")
    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable Long id) {

         officeService.deleteOffice(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Внести изменения в существующий офис")
    public OfficeResponse updateOffice(@PathVariable Long id, @RequestBody OfficeRequest request) {

        return officeService.updateOffice(id, request);
    }

    @PutMapping("/{officeId}/{userId}")
    @Operation(summary = "Добавить в офис нового сотрудника")
    public OfficeResponse userToOffice(@PathVariable Long officeId, @PathVariable Long userId) {

        return officeService.userToOffice(officeId, userId);
    }

}
