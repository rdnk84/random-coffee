package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserDto(Long id);

    List<UserResponse> allUsers();

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse createUser(UserRequest request);

    void deleteUser(Long id);
}
