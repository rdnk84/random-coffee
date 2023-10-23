package com.example.randomcoffee.service;

import com.example.randomcoffee.rest_api.dto.UserRequest;
import com.example.randomcoffee.rest_api.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserDto(Long id);

    List<UserResponse> allUsers();

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse createUser(UserRequest request);

    void deleteUser(Long id);
}
