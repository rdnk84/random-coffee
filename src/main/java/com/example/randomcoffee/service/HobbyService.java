package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface HobbyService {

    HobbyResponse getHobbyDto(Long id);
    Page<HobbyResponse> getAllHobbies(Integer page, Integer perPage, String sort, Sort.Direction order);

    HobbyResponse createHobby(HobbyRequest request);
    HobbyResponse updateHobby(Long id, HobbyRequest request);
    void deleteHobby(Long id);
    Page<HobbyResponse> getHobbiesByTitle(Integer page, Integer perPage, String sort, Sort.Direction order, String title);
}
