package com.example.randomcoffee.rest_api.rest_controllers;

import com.example.randomcoffee.model.enums.Region;
import com.example.randomcoffee.rest_api.dto.request.CountryRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.example.randomcoffee.service.impl.CountryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Random-coffee")
@RestController
@RequestMapping("/api/country")
@RequiredArgsConstructor
public class CountryRestController {

    private final CountryServiceImpl countryService;

    @Operation(summary = "Получить страну по id")
    @GetMapping("/{id}")
    CountryResponse getCountry(@PathVariable Long id) {
        return countryService.getCountryById(id);
    }

    @Operation(summary = "Внести новую страну")
    @PostMapping("/")
    CountryResponse updateCountry(@RequestBody CountryRequest request) {
        return countryService.createCountry(request);
    }

    @Operation(summary = "Внести изменения в страну")
    @PutMapping("/{id}")
    CountryResponse updateCountry(@PathVariable Long id, @RequestBody CountryRequest request) {
        return countryService.changeCountry(id, request);
    }

    @Operation(summary = "Найти все страны по региону")
    @GetMapping("/by-region")
    public List<CountryResponse> findByRegion(@RequestParam Region region) {
        return countryService.countriesByRegion(region);
    }

    @Operation(summary = "Найти страну по названию")
    @GetMapping("/by-title/")
    public CountryResponse findByRegion(@RequestParam String title) {
        return countryService.countryByTitle(title);
    }

    @Operation(summary = "Удалить страну")
    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id){
        countryService.deleteCountry(id);
    }
}
