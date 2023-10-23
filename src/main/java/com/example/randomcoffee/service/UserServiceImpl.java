package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.User;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.rest_api.dto.UserRequest;
import com.example.randomcoffee.rest_api.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final ObjectMapper mapper;


    @Override
    public UserResponse getUserDto(Long id) {
        User user = userRepo.findById(id).orElse(new User());
        UserResponse userFound = mapper.convertValue(user, UserResponse.class);
        return userFound;
    }

    @Override
    public List<UserResponse> allUsers() {
        return null;
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        return null;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
//        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
//            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
//        }
//        String email = request.getEmail().trim();
//        userRepo.findByEmail(email)
//                .ifPresent(u -> {
//                    throw new CustomException("User with this email already exist", HttpStatus.BAD_REQUEST);
//                });
//        if (StringUtils.isBlank(request.getLastName()) || StringUtils.isBlank(request.getFirstName()) || StringUtils.isBlank(request.getPassword())) {
//            throw new CustomException("Some of highlighted fields are blank", HttpStatus.BAD_REQUEST);
//        }
        User user = mapper.convertValue(request, User.class);
        user.setCreatedAt(LocalDateTime.now());

        User save = userRepo.save(user);
        UserResponse createdUser = mapper.convertValue(save, UserResponse.class);

        return createdUser;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
