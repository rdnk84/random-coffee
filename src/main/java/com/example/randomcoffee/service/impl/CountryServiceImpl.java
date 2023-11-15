package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Country;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.CountryRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.model.enums.Region;
import com.example.randomcoffee.rest_api.dto.request.CountryRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;
import com.example.randomcoffee.rest_api.dto.response.EventResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepo countryRepo;
    private final ObjectMapper mapper;

    @Override
    public CountryResponse getCountryById(Long id) {
        String errorMsg = String.format("Country with id %d not found", id);
        Country country = countryRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        CountryResponse result = mapper.convertValue(country, CountryResponse.class);
        return result;
    }



    @Override
    public CountryResponse changeCountry(Long id, CountryRequest request) {
        String errorMsg = String.format("Country with id %d not found", id);
        Country country = countryRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        country.setTitle(StringUtils.isBlank(request.getTitle()) ? country.getTitle() : request.getTitle());
        country.setRegion(request.getRegion() == null ? country.getRegion() : request.getRegion());
        country.setUpdatedAt(LocalDateTime.now());
        country.setStatus(EntityStatus.UPDATED);
        Country save = countryRepo.save(country);
        return mapper.convertValue(save, CountryResponse.class);
    }

    @Override
    public List<CountryResponse> countriesByRegion(Region region) {
        List<Country> countries = countryRepo.findByRegion(region);
        if (countries != null) {
            List<CountryResponse> result = countries.stream()
                    .map(c -> mapper.convertValue(c, CountryResponse.class))
                    .collect(Collectors.toList());
            return result;
        }
        throw new CustomException("No country under the region found", HttpStatus.NOT_FOUND);
    }

    @Override
    public CountryResponse countryByTitle(String title) {
        String errorMsg = String.format("User with title %s not found", title);
        Country country = countryRepo.findByTitle(title).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        CountryResponse result = mapper.convertValue(country, CountryResponse.class);
        return result;
    }

    @Override
    public CountryResponse createCountry(CountryRequest request) {
        String countryTitle = request.getTitle();
        countryRepo.findByTitle(countryTitle)
                .ifPresent(c -> {
                    throw new CustomException("This country already exists", HttpStatus.BAD_REQUEST);
                });
        Country country = mapper.convertValue(request, Country.class);
        country.setCreatedAt(LocalDateTime.now());
        country.setStatus(EntityStatus.CREATED);
        Country save = countryRepo.save(country);
        CountryResponse result = mapper.convertValue(save, CountryResponse.class);
        return result;
    }

    @Override
    public void deleteCountry(Long id) {
        String errorMsg = String.format("User with id %d not found", id);
        Country country = countryRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!country.getStatus().equals(EntityStatus.DELETED)) {
            country.setStatus(EntityStatus.DELETED);
            country.setUpdatedAt(LocalDateTime.now());
            countryRepo.save(country);
            return;
        }
        throw new CustomException("The country already has deleted", HttpStatus.NOT_FOUND);
    }
}
