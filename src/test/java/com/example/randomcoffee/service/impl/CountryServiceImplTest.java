package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.repository.CountryRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.model.enums.Region;
import com.example.randomcoffee.rest_api.dto.request.CountryRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CountryServiceImplTest {

    @InjectMocks
    CountryServiceImpl countryService;

    @Mock
    CountryRepo countryRepo;

    @Spy
    ObjectMapper objectMapper;

    @Test
    void getCountryById() {
        Country country = new Country();
        country.setId(1L);
        when(countryRepo.findById(country.getId())).thenReturn(Optional.of(country));
        CountryResponse result = countryService.getCountryById(country.getId());
        assertEquals(Optional.of(result.getId()), Optional.of(country.getId()));
    }

    @Test
    void country_not_found() {
        Country country = new Country();
        country.setId(1L);
        when(countryRepo.findById(5L)).thenThrow(CustomException.class);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            countryService.getCountryById(5L);
        }, "Country with  this id %d not found");
    }

    @Test
    void changeCountry() {

        CountryRequest request = new CountryRequest();
        request.setTitle("Moscow");

        Country country = new Country();
        country.setId(1L);
        country.setTitle("Spb");
        country.setRegion(Region.EMEA);
        when(countryRepo.findById(country.getId())).thenReturn(Optional.of(country));
        when(countryRepo.save(ArgumentMatchers.any(Country.class))).thenReturn(country);

        CountryResponse result = countryService.changeCountry(country.getId(), request);
        assertEquals(result.getRegion(), country.getRegion());
        assertEquals(result.getTitle(), request.getTitle());
    }

    @Test
    void countriesByRegion() {
        Country country = new Country();
        country.setId(1L);
        country.setRegion(Region.NA);

        List<Country> countries = (List.of(country));
        when(countryRepo.findByRegion(country.getRegion())).thenReturn(countries);
        List<CountryResponse> result = countryService.countriesByRegion(country.getRegion());
        assertEquals(result.size(), 1);
    }

    @Test
    void countryByTitle() {
        Country country = new Country();
        country.setId(1L);
        country.setTitle("Spb");
        when(countryRepo.findByTitle(country.getTitle())).thenReturn(Optional.of(country));
        CountryResponse result = countryService.countryByTitle(country.getTitle());

        assertEquals(result.getTitle(), country.getTitle());
    }

    @Test
    void createCountry() {
        CountryRequest request = new CountryRequest();
        request.setTitle("Moscow");
        request.setRegion(Region.EMEA);

        Country country = new Country();
        country.setId(1L);
        when(countryRepo.save(ArgumentMatchers.any(Country.class))).thenReturn(country);
        CountryResponse result = countryService.createCountry(request);
        assertEquals(Optional.of(result.getId()), Optional.of(country.getId()));
    }

    @Test
    void deleteCountry() {
        Country country = new Country();
        country.setId(1L);
        country.setStatus(EntityStatus.CREATED);
        when(countryRepo.findById(country.getId())).thenReturn(Optional.of(country));
        countryService.deleteCountry(country.getId());

        verify(countryRepo, times(1)).save(ArgumentMatchers.any(Country.class));
    }
}