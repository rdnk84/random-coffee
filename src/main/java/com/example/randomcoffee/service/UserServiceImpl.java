package com.example.randomcoffee.service;

import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.rest_api.dto.UserRequest;
import com.example.randomcoffee.rest_api.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        CoffeeUser coffeeUser = userRepo.findById(id).orElse(new CoffeeUser());
        UserResponse userFound = mapper.convertValue(coffeeUser, UserResponse.class);
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
//                    throw new CustomException("CoffeeUser with this email already exist", HttpStatus.BAD_REQUEST);
//                });
//        if (StringUtils.isBlank(request.getLastName()) || StringUtils.isBlank(request.getFirstName()) || StringUtils.isBlank(request.getPassword())) {
//            throw new CustomException("Some of highlighted fields are blank", HttpStatus.BAD_REQUEST);
//        }
        CoffeeUser coffeeUser = mapper.convertValue(request, CoffeeUser.class);
        coffeeUser.setCreatedAt(LocalDateTime.now());

        CoffeeUser save = userRepo.save(coffeeUser);
        UserResponse createdUser = mapper.convertValue(save, UserResponse.class);

        return createdUser;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
