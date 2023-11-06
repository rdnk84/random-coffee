package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.entity.MeetingEvent;
import com.example.randomcoffee.model.db.repository.EventRepo;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EventRepo eventRepo;
    private final ObjectMapper mapper;


    @Override
    public UserResponse getUserDto(Long id) {

        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        UserResponse userFound = mapper.convertValue(user, UserResponse.class);
        return userFound;
    }

    @Override
    public Page<UserResponse> usersByLastName(Integer page, Integer perPage, String sort, Sort.Direction order, String lastName) {
        Pageable pageRequest = PaginationUtil.getPageRequest(page, perPage, sort, order);
        Page<CoffeeUser> usersPage = userRepo.findByLastNameNotDeleted(pageRequest, lastName);
        List<UserResponse> usersList = usersPage.getContent().stream()
                .map(u -> mapper.convertValue(u, UserResponse.class))
                .collect(Collectors.toList());
        return new PageImpl<>(usersList);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        String errorMsg = String.format("User with id %d not found", id);
        CoffeeUser user = userRepo.findById(id).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));

        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
        }

        user.setFirstName(StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName());
        user.setLastName(StringUtils.isBlank(request.getLastName()) ? user.getLastName() : request.getLastName());
        user.setEmail(StringUtils.isBlank(request.getEmail()) ? user.getEmail() : request.getEmail());
        user.setPassword(StringUtils.isBlank(request.getPassword()) ? user.getPassword() : request.getPassword());
        user.setMiddleName(StringUtils.isBlank(request.getMiddleName()) ? user.getMiddleName() : request.getMiddleName());
        user.setFirstName(StringUtils.isBlank(request.getFirstName()) ? user.getFirstName() : request.getFirstName());
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
    public String handleEvent(Boolean accept, Long eventId, Long userId) {
        String userNotFound = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(userNotFound, HttpStatus.NOT_FOUND));
        String eventNotFound = String.format("Event with id %d not found", eventId);
        MeetingEvent event = eventRepo.findById(eventId).orElseThrow(() -> new CustomException(eventNotFound, HttpStatus.NOT_FOUND));
        String answer = "";
        if(accept = false) {
            Set<CoffeeUser> participants = event.getParticipants();
            participants.remove(user);
            eventRepo.save(event);
            answer = "This colleague has declined the meeting";
            return answer;
        }
        answer = "This colleague has accepted the meeting";
        return answer;
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

    public Set<MeetingEvent> checkAllEvents(Long userId) {
        String errorMsg = String.format("User with id %d not found", userId);
        CoffeeUser user = userRepo.findById(userId).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        Set<MeetingEvent> events = user.getMeetingEvents();
        return events;
    }



}
