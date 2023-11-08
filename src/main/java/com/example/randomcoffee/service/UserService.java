package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {

    UserResponse getUserDto(Long id);

    Page<UserResponse> usersByLastName(Integer page, Integer perPage, String sort, Sort.Direction order, String filter);

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse createUser(UserRequest request);

    void deleteUser(Long id);

    String declineEvent(Long eventId, Long userId);
    String acceptEvent(Long eventId, Long userId);
    Page<UserResponse> allUsers(Integer page, Integer perPage, String sort, Sort.Direction order);
}
