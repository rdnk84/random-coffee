package com.example.randomcoffee.config.auth;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.Role;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

//    public AuthenticationResponse register(RegisterRequest request) {
//
//        String email = request.getEmail().trim();
//        userRepo.findByEmail(email)
//                .ifPresent(u -> {
//                    throw new CustomException("CoffeeUser with this email already exist", HttpStatus.BAD_REQUEST);
//                });
//        if (StringUtils.isBlank(request.getLastName()) || StringUtils.isBlank(request.getFirstName()) || StringUtils.isBlank(request.getPassword())) {
//            throw new CustomException("Some of highlighted fields are blank", HttpStatus.BAD_REQUEST);
//        }
//        if (!EmailValidator.getInstance().isValid(email)) {
//            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
//        }
//        CoffeeUser user = CoffeeUser.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
//        userRepo.save(user);
//        String jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }

    public AuthenticationResponse register(CoffeeUser request) {

        String email = request.getEmail().trim();
        userRepo.findByEmail(email)
                .ifPresent(u -> {
                    throw new CustomException("CoffeeUser with this email already exist", HttpStatus.BAD_REQUEST);
                });
        if (StringUtils.isBlank(request.getLastName()) || StringUtils.isBlank(request.getFirstName()) || StringUtils.isBlank(request.getPassword())) {
            throw new CustomException("Some of highlighted fields are blank", HttpStatus.BAD_REQUEST);
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new CustomException("invalid email", HttpStatus.BAD_REQUEST);
        }
        CoffeeUser user = CoffeeUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String email = request.getEmail().trim();
        String errorMsg = String.format("User with email %s not found", email);
        CoffeeUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
