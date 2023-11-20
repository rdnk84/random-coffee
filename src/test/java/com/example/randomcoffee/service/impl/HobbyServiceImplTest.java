package com.example.randomcoffee.service.impl;

import com.example.randomcoffee.model.db.repository.HobbyRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void getAllHobbies() {
    }

    @Test
    void createHobby() {
    }

    @Test
    void updateHobby() {
    }

    @Test
    void deleteHobby() {
    }

    @Test
    void getHobbyByTitle() {
    }
}