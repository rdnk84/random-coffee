package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.model.db.entity.Hobby;
import com.example.randomcoffee.model.db.entity.Office;
import com.example.randomcoffee.model.db.entity.Project;
import com.example.randomcoffee.model.db.repository.HobbyRepo;
import com.example.randomcoffee.model.enums.EntityStatus;
import com.example.randomcoffee.rest_api.dto.request.HobbyRequest;
import com.example.randomcoffee.rest_api.dto.request.ProjectRequest;
import com.example.randomcoffee.rest_api.dto.response.HobbyResponse;
import com.example.randomcoffee.rest_api.dto.response.OfficeResponse;
import com.example.randomcoffee.rest_api.dto.response.ProjectResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.randomcoffee.utils.PaginationUtil.getPageRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class HobbyServiceImplTest {

    @InjectMocks
    HobbyServiceImpl hobbyService;

    @Mock
    HobbyRepo hobbyRepo;

    @Spy
    ObjectMapper mapper;

    @Test
    void getHobbyDto() {
        Hobby hobby = new Hobby();
        hobby.setId(1L);
        when(hobbyRepo.findById(hobby.getId())).thenReturn(Optional.of(hobby));
        HobbyResponse result = hobbyService.getHobbyDto(hobby.getId());
        assertEquals(Optional.of(result.getId()), Optional.of(hobby.getId()));
    }

    @Test
    void getAllHobbies() {
        Hobby hobby = new Hobby();
        hobby.setId(1L);

        Set<Hobby> hobbySetSet = (Set.of(hobby));
        List<Hobby> hobbyList = hobbySetSet.stream().toList();
        Pageable pageRequest = getPageRequest(1, 2, "title", Sort.Direction.ASC);
        Page<Hobby> projectPage = new PageImpl<Hobby>(hobbyList, pageRequest, 1);
        when(hobbyRepo.findAll(any(Pageable.class))).thenReturn(projectPage);
        Page<HobbyResponse> result = hobbyService.getAllHobbies(1, 2, "title", Sort.Direction.ASC);
        assertEquals(result.getNumberOfElements(), 1);
    }

    @Test
    void createHobby() {

        HobbyRequest request = new HobbyRequest();
        request.setTitle("title");
        request.setDescription("some description");

        Hobby hobby = new Hobby();
        hobby.setId(1L);

        when(hobbyRepo.save(ArgumentMatchers.any(Hobby.class))).thenReturn(hobby);
        HobbyResponse result = hobbyService.createHobby(request);
        assertEquals(result.getId(), hobby.getId());
        assertEquals(result.getTitle(), hobby.getTitle());
    }

    @Test
    void updateHobby() {
        HobbyRequest request = new HobbyRequest();
        request.setTitle("another title");

        Hobby hobby = new Hobby();
        hobby.setId(1L);
        hobby.setDescription("JJJJ");
        when(hobbyRepo.findById(hobby.getId())).thenReturn(Optional.of(hobby));
        when(hobbyRepo.save(ArgumentMatchers.any(Hobby.class))).thenReturn(hobby);

        HobbyResponse result = hobbyService.updateHobby(hobby.getId(), request);
        assertEquals(result.getTitle(), request.getTitle());
        assertEquals(result.getDescription(), hobby.getDescription());
    }

    @Test
    void deleteHobby() {
        Hobby hobby = new Hobby();
        hobby.setId(1L);
        hobby.setStatus(EntityStatus.CREATED);
        when(hobbyRepo.findById(hobby.getId())).thenReturn(Optional.of(hobby));
       hobbyService.deleteHobby(hobby.getId());
        verify(hobbyRepo, times(1)).save(ArgumentMatchers.any(Hobby.class));
    }

    @Test
    void getHobbyByTitle() {
        Hobby hobby = new Hobby();
        hobby.setId(1L);
        hobby.setTitle("some hobby");
        List<Hobby> hobbies = (List.of(hobby));
        Pageable pageRequest = getPageRequest(1, 2, "title", Sort.Direction.ASC);
        Page<Hobby> hobbyPage = new PageImpl<Hobby>(hobbies, pageRequest, 1);
        when(hobbyRepo.findByTitle(any(Pageable.class), anyString())).thenReturn(hobbyPage);
        Page<HobbyResponse> result = hobbyService.getHobbyByTitle(1, 2, "title", Sort.Direction.ASC, hobby.getTitle());
        assertEquals(result.getNumberOfElements(), 1);
    }
}