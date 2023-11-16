package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.entity.Office;
import com.example.randomcoffee.model.db.entity.Project;
import com.example.randomcoffee.model.db.repository.EventRepo;
import com.example.randomcoffee.model.db.repository.OfficeRepo;
import com.example.randomcoffee.model.db.repository.ProjectRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.OfficeStatus;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.example.randomcoffee.rest_api.dto.request.OfficeRequest;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.example.randomcoffee.service.UserService;
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

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;
    private final ObjectMapper mapper;
    private final OfficeRepo officeRepo;
    private final ProjectRepo projectRepo;


    @Override
    public UserResponse getUserDto(Long id) {

        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        UserResponse userFound = mapper.convertValue(user, UserResponse.class);
        return userFound;
    }

    public CoffeeUser coffeeUserById(Long id) {

        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        return user;
    }

    @Override
    public Page<UserResponse> usersByLastName(Integer page, Integer perPage, String sort, Sort.Direction order, String lastName) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<CoffeeUser> usersPage = userRepo.findByLastNameNotDeleted(pageRequest, lastName);
//        Page<CoffeeUser> usersPage = userRepo.findByLastName(pageRequest, lastName);
        List<UserResponse> usersList = usersPage.getContent().stream()
                .map(u -> mapper.convertValue(u, UserResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usersList);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        String email = request.getEmail();
        if (StringUtils.isBlank(email)) {
            email = user.getEmail();
        } else if (!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
        } else {
            email = request.getEmail();
        }
        user.setEmail(email);
//        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
//            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
//        }

        user.setFirstName(StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName());
        user.setLastName(StringUtils.isBlank(request.getLastName()) ? user.getLastName() : request.getLastName());

        user.setPassword(StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword());
        user.setMiddleName(StringUtils.isBlank(request.getMiddleName()) ? user.getMiddleName() : request.getMiddleName());
        user.setAstroSign(request.getAstroSign() == null ? user.getAstroSign() : request.getAstroSign());
        user.setDepartment(request.getDepartment() == null ? user.getDepartment() : request.getDepartment());

        user.setUpdatedAt(LocalDateTime.now());

        CoffeeUser save = userRepo.save(user);
        return mapper.convertValue(save, UserResponse.class);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
        }
        String email = request.getEmail().trim();
        userRepo.findByEmail(email)
                .ifPresent(u -> {
                    throw new CustomException("CoffeeUser with this email already exist", HttpStatus.BAD_REQUEST);
                });
        if (StringUtils.isBlank(request.getLastName()) || StringUtils.isBlank(request.getFirstName()) || StringUtils.isBlank(request.getPassword())) {
            throw new CustomException("Some of highlighted fields are blank", HttpStatus.BAD_REQUEST);
        }
        CoffeeUser coffeeUser = mapper.convertValue(request, CoffeeUser.class);
        coffeeUser.setCreatedAt(LocalDateTime.now());
        coffeeUser.setStatus(UserActivityStatus.CREATED);

        CoffeeUser save = userRepo.save(coffeeUser);
        UserResponse createdUser = mapper.convertValue(save, UserResponse.class);
        return createdUser;
    }


    @Override
    public void deleteUser(Long id) {
        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus(UserActivityStatus.DELETED);
        userRepo.save(user);
    }

    @Override
    public Page<UserResponse> allUsers(Integer page, Integer perPage, String sort, Sort.Direction order) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<CoffeeUser> usersPage = userRepo.findAll(pageRequest);

        List<UserResponse> usersList = usersPage.getContent().stream()
                .map(u -> mapper.convertValue(u, UserResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usersList);
    }

    public Set<MeetingEvent> checkAllUserEvents(Long userId) {
        String errorMsg = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        Set<MeetingEvent> events = user.getEvents();
        return events;
    }

    @Override
    public UserResponse userChangeOffice(Long userId, Long newOfficeId) {
        String errorMsg = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        Office oldOffice = user.getOffice();
        String newOfficeNotFound = String.format("Office with id %d not found", newOfficeId);
        Office newOffice = officeRepo.findById(newOfficeId).orElseThrow(() -> new CustomException(newOfficeNotFound, HttpStatus.NOT_FOUND));
        user.setOffice(newOffice);
        user.setUpdatedAt(LocalDateTime.now());
        oldOffice.getColleagues().remove(user);
        newOffice.getColleagues().add(user);
        newOffice.setStatus(OfficeStatus.UPDATED);
        newOffice.setUpdatedAt(LocalDateTime.now());
        oldOffice.setStatus(OfficeStatus.UPDATED);
        oldOffice.setUpdatedAt(LocalDateTime.now());
        officeRepo.save(oldOffice);
        officeRepo.save(newOffice);

        CoffeeUser save = userRepo.save(user);

        UserResponse result = mapper.convertValue(save, UserResponse.class);
        OfficeResponse officeResponse = mapper.convertValue(newOffice, OfficeResponse.class);
        result.setOffice(officeResponse);
        return result;
    }

    @Override
    public Page<UserResponse> findByHiringPeriod(Integer page, Integer perPage, String sort, Sort.Direction order, String fromDate, String toDate) {
        Date sqlFromDate = Date.valueOf(fromDate);
        Date sqlToDate = Date.valueOf(toDate);
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<CoffeeUser> usersPage = userRepo.findByHiringDate(pageRequest, sqlFromDate, sqlToDate);
        List<UserResponse> usersList = usersPage.getContent().stream()
                .map(u -> mapper.convertValue(u, UserResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usersList);
    }

    @Override
    public UserResponse addProject(Long userId, Long projectId) {
        String userNotFound = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));

        String errorMsg = String.format("Project with id %d not found", projectId);
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        List<Project> projects = user.getProjects();
        projects.add(project);
        user.setProjects(projects);

        List<CoffeeUser> colleagues = project.getColleagues();
        colleagues.add(user);
        project.setColleagues(colleagues);

        projectRepo.save(project);
        CoffeeUser save = userRepo.save(user);

        UserResponse result = mapper.convertValue(save, UserResponse.class);
        List<ProjectResponse> projectResponseList = projects.stream()
                        .map(u -> mapper.convertValue(u, ProjectResponse.class))
                                .collect(Collectors.toList());
        result.setUsersProjects(projectResponseList);

        return result;
    }

    @Override
    public Page<UserResponse> getUsersByProject(Integer page, Integer perPage, String sort, Sort.Direction order, String projectCode) {

        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<CoffeeUser> usersPage = userRepo.findUsersByProjectCode(pageRequest, projectCode);
        List<UserResponse> usersList = usersPage.getContent().stream()
                .map(u -> mapper.convertValue(u, UserResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usersList);
    }

//    @Override
//    public List<UserResponse> getUsersByProject(String projectCode) {
//
//        Project project = projectRepo.findByProjectCode(projectCode).orElseThrow(() -> new CustomException("project not found", HttpStatus.NOT_FOUND));
//        List<CoffeeUser> usersList = project.getColleagues();
//        List<UserResponse> result = usersList.stream()
//                .map(u -> mapper.convertValue(u, UserResponse.class))
//                .collect(Collectors.toList());
//        return result;
//    }

}
