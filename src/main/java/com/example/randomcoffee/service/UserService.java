package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.List;

public interface UserService {

    UserResponse getUserDto(Long id);

    Page<UserResponse> usersByLastName(Integer page, Integer perPage, String sort, Sort.Direction order, String filter);

    UserResponse updateUser(Long id, UserRequest request);

    UserResponse createUser(UserRequest request);

    void deleteUser(Long id);

    Page<UserResponse> allUsers(Integer page, Integer perPage, String sort, Sort.Direction order);

    UserResponse userChangeOffice(Long userId, Long newOfficeId);

    Page<UserResponse> findByHiringPeriod(Integer page, Integer perPage, String sort, Sort.Direction order, String fromDate, String toDate);

    UserResponse addProject(Long userId, Long projectId);

       Page<UserResponse> getUsersByProject(Integer page, Integer perPage, String sort, Sort.Direction order, String projectCode);
//    List<UserResponse> getUsersByProject(String projectCode);
}
