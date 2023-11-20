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
import com.example.randomcoffee.model.enums.AstroSign;
import com.example.randomcoffee.model.enums.Department;
import com.example.randomcoffee.model.enums.UserActivityStatus;
import com.example.randomcoffee.rest_api.dto.request.UserRequest;
import com.example.randomcoffee.rest_api.dto.response.UserResponse;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.randomcoffee.utils.PaginationUtil.getPageRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Spy
    ObjectMapper mapper;

    public ObjectMapper getMapper() {
        return mapper;
    }

    @Mock
    UserRepo userRepo;

    @Mock
    EventRepo eventRepo;

    @Mock
    OfficeRepo officeRepo;

    @Mock
    ProjectRepo projectRepo;

    @Test
    void getUserDto() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        UserResponse result = userService.getUserDto(user.getId());
        assertEquals(Optional.of(result.getId()), Optional.of(user.getId()));
    }

    @Test
    void user_not_found() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        when(userRepo.findById(5L)).thenThrow(CustomException.class);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            userService.coffeeUserById(5L);
        }, "CoffeeUser with this id not found");
    }

    @Test
    void usersByLastName() {
        String lastName = "Petrov";
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        user.setEmail("petrov@mail.ru");
        user.setLastName(lastName);

        List<CoffeeUser> users = (List.of(user));
        Pageable pageRequest = getPageRequest(1, 2, "email", Sort.Direction.ASC);
        Page<CoffeeUser> usersPage = new PageImpl<CoffeeUser>(users, pageRequest, 1);

        when(userRepo.findByLastNameNotDeleted(any(Pageable.class), anyString())).thenReturn(usersPage);

        Page<UserResponse> result = userService.usersByLastName(1, 2, "email", Sort.Direction.ASC, lastName);
        assertEquals(result.getNumberOfElements(), 1);
    }

    @Test
    void updateUser() {
        UserRequest request = new UserRequest();
        request.setAstroSign(AstroSign.CAPRICORNUS);

        CoffeeUser user = new CoffeeUser();
        user.setEmail("petrov@mail.com");
        user.setPassword("jjjjj");
        user.setLastName("Petrov");
        user.setFirstName("Pavel");
        user.setMiddleName("Olegovich");
        user.setDepartment(Department.CEO);
//        user.setHiringDate("2018-07-28");
        user.setAstroSign(AstroSign.AQUARIUS);
        user.setId(1L);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);
        UserResponse result = userService.updateUser(1L, request);
        assertEquals(result.getAstroSign(), request.getAstroSign());
        assertEquals(result.getEmail(), user.getEmail());
    }


//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        objectMapper.registerModule(new JavaTimeModule());
//        return objectMapper;
//    }


    @Test
    void createUser() {
        UserRequest request = new UserRequest();
        request.setEmail("petrov@mail.ru");
        request.setPassword("jjjjj");
        request.setLastName("Petrov");
        request.setFirstName("Pavel");
        request.setMiddleName("Olegovich");
        request.setDepartment(Department.CEO);
//        request.setHiringDate(LocalDate.of(2019, 04, 16));

        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
//        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);
        UserResponse result = userService.createUser(request);

        assertEquals(result.getId(), user.getId());
    }

    @Test
    void email_already_exists() {
        UserRequest request = new UserRequest();
        request.setEmail("petrov@mail.ru");
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        user.setEmail("petrov@mail.ru");
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        when(userRepo.save(any(CoffeeUser.class))).thenThrow(CustomException.class);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            userService.createUser(request);
        }, "CoffeeUser with this email already exist");
    }

    @Test
    void invalid_email() {
        UserRequest request = new UserRequest();
        request.setEmail("petrov@mail.uk.com");

        when(userRepo.save(any(CoffeeUser.class))).thenThrow(CustomException.class);
        CustomException thrown = Assertions.assertThrows(CustomException.class, () -> {
            userService.createUser(request);
        }, "invalid email");
    }

    @Test
    void deleteUser() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        user.setStatus(UserActivityStatus.CREATED);

        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepo, times(1)).save(any(CoffeeUser.class));
        assertEquals(UserActivityStatus.DELETED, user.getStatus());
    }

    @Test
    void allUsers() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        user.setEmail("petrov@mail.ru");

        List<CoffeeUser> users = (List.of(user));
        Pageable pageRequest = getPageRequest(1, 2, "email", Sort.Direction.ASC);
        Page<CoffeeUser> usersPage = new PageImpl<CoffeeUser>(users, pageRequest, 1);
        when(userRepo.findAll(any(Pageable.class))).thenReturn(usersPage);

        Page<UserResponse> result = userService.allUsers(1, 2, "email", Sort.Direction.ASC);
        assertEquals(result.getNumberOfElements(), 1);
    }

    @Test
    void checkAllUserEvents() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);

        MeetingEvent event = new MeetingEvent();
        event.setId(12L);
        when(eventRepo.findById(event.getId())).thenReturn(Optional.of(event));
        Set<MeetingEvent> events = (Set.of(event));
        user.setEvents(events);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));
        userService.checkAllUserEvents(user.getId());
        assertEquals(user.getEvents().size(), 1);
    }

    @Test
    void userChangeOffice() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        Office officeOld = new Office();
        officeOld.setId(1L);
        when(officeRepo.findById(1L)).thenReturn(Optional.of(officeOld));
        user.setOffice(officeOld);

        Office officeNew = new Office();
        officeNew.setId(2L);
        when(officeRepo.findById(2L)).thenReturn(Optional.of(officeNew));

        when(officeRepo.save(any(Office.class))).thenReturn(officeOld);
        when(officeRepo.save(any(Office.class))).thenReturn(officeNew);
        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);

        UserResponse result = userService.userChangeOffice(1L, officeNew.getId());
        assertEquals(result.getOffice().getId(), officeNew.getId());
    }

    @Test
    void addProject() {
        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        Project project = new Project();
        project.setId(5L);
        when(projectRepo.findById(project.getId())).thenReturn(Optional.of(project));

        when(userRepo.save(any(CoffeeUser.class))).thenReturn(user);
        when(projectRepo.save(any(Project.class))).thenReturn(project);

        UserResponse result = userService.addProject(1L, 5L);
        assertEquals(result.getUsersProjects().size(), 1);
        assertEquals(project.getColleagues().size(), 1);
    }

    @Test
    void getUsersByProject() {

        CoffeeUser user = new CoffeeUser();
        user.setId(1L);
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        Project project = new Project();
        project.setId(5L);
        project.setProjectCode("FNCH");
        when(projectRepo.findByProjectCode(project.getProjectCode())).thenReturn(Optional.of(project));

        List<CoffeeUser> userList = (List.of(user));
        Pageable pageRequest = getPageRequest(1, 2, "email", Sort.Direction.ASC);
        Page<CoffeeUser> userPage = new PageImpl<CoffeeUser>(userList, pageRequest, 1);
        when(userRepo.findUsersByProjectCode(any(Pageable.class), anyString())).thenReturn(userPage);
        Page<UserResponse> result = userService.getUsersByProject(1, 2, "email", Sort.Direction.ASC, project.getProjectCode());
        assertEquals(result.getNumberOfElements(), 1);
        //        user.setProjects(projects);
//
//        List<CoffeeUser> colleaguegsList = (List.of(user));
//        project.setColleagues(colleaguegsList);
//        List<UserResponse> userResponseList = userService.getUsersByProject(project.getProjectCode());
//        assertEquals(userResponseList.size(), 1);
    }

//    @Test
//    void findByHiringPeriod() {
//
//    }
}