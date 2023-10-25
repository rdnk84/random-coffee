package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.service.impl.OfficeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/office")
@RequiredArgsConstructor
public class OfficeRestController {

    private final OfficeServiceImpl officeService;

    @PostMapping("/")
    @Operation(summary = "Create new office")
    public OfficeResponse createOffice(@RequestBody OfficeRequest request) {
        return officeService.createOffice(request);
    }

    @Operation(summary = "Get office by id")
    @GetMapping("/{id}")
    public OfficeResponse getOffice(@PathVariable Long id) {
        return officeService.getOfficeById(id);
    }
}
