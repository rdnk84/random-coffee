package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.Hobby;
import com.example.randomcoffee.model.db.repository.HobbyRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.HobbyService;
import com.example.randomcoffee.utils.PaginationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HobbyServiceImpl implements HobbyService {

    private final HobbyRepo hobbyRepo;
    private final ObjectMapper objectMapper;

    @Override
    public HobbyResponse getHobbyDto(Long id) {
        String errorMsg = String.format("Hobby with id %d not found", id);
        Hobby hobby = hobbyRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        HobbyResponse result = objectMapper.convertValue(hobby, HobbyResponse.class);
        return result;
    }

    @Override
    public Page<HobbyResponse> getAllHobbies(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Hobby> hobbiesPage = hobbyRepo.findAll(pageRequest);
        List<HobbyResponse> hobbiesList = hobbiesPage.getContent().stream()
                .map(h -> objectMapper.convertValue(h, HobbyResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(hobbiesList);
    }

    @Override
    public HobbyResponse createHobby(HobbyRequest request) {

        if (StringUtils.isBlank(request.getTitle())) {
            throw new CustomException("This field can not be empty", HttpStatus.BAD_REQUEST);
        }
        Hobby hobby = objectMapper.convertValue(request, Hobby.class);
        hobby.setCreatedAt(LocalDateTime.now());
        hobby.setStatus(EntityStatus.CREATED);
        Hobby save = hobbyRepo.save(hobby);
        HobbyResponse result = objectMapper.convertValue(save, HobbyResponse.class);
        return result;
    }

    @Override
    public HobbyResponse updateHobby(Long id, HobbyRequest request) {
        String errorMsg = String.format("Hobby with id %d not found", id);
        Hobby hobby = hobbyRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        hobby.setTitle(StringUtils.isBlank(request.getTitle()) ? hobby.getTitle() : request.getTitle());
        hobby.setDescription(StringUtils.isBlank(request.getDescription()) ? hobby.getDescription() : request.getDescription());
        hobby.setUpdatedAt(LocalDateTime.now());
        hobby.setStatus(EntityStatus.UPDATED);
        Hobby save = hobbyRepo.save(hobby);
        return objectMapper.convertValue(save, HobbyResponse.class);
    }

    @Override
    public void deleteHobby(Long id) {
        String errorMsg = String.format("Hobby with id %d not found", id);
        Hobby hobby = hobbyRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        if (!hobby.getStatus().equals(EntityStatus.DELETED)) {
            hobby.setStatus(EntityStatus.DELETED);
            hobby.setUpdatedAt(LocalDateTime.now());
            hobbyRepo.save(hobby);
            return;
        }
        throw new CustomException("This hobby was already deleted", HttpStatus.NOT_FOUND);
    }

    @Override
    public Page<HobbyResponse> getHobbiesByTitle(Integer page, Integer perPage, String sort, Sort.Direction order, String title) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<Hobby> hobbiesPage = hobbyRepo.findByTitle(pageRequest, title);
        List<HobbyResponse> hobbiesList = hobbiesPage.getContent().stream()
                .map(h -> objectMapper.convertValue(h, HobbyResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(hobbiesList);
    }
}
