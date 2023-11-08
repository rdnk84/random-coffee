package com.example.randomcoffee.service;

import com.example.randomcoffee.model.enums.Region;
import com.example.randomcoffee.rest_api.dto.request.CountryRequest;
import com.example.randomcoffee.rest_api.dto.response.CountryResponse;

import java.util.List;

public interface CountryService {

    CountryResponse getCountryById(Long id);

    CountryResponse changeCountry(Long id, CountryRequest request);

    List<CountryResponse> countriesByRegion(Region region);

    CountryResponse createCountry(CountryRequest request);

    void deleteCountry(Long id);
    CountryResponse countryByTitle(String title);

}
