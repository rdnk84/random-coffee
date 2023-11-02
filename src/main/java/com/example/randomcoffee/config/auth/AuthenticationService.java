package com.example.randomcoffee.config.auth;

import com.example.randomcoffee.exceptions.CustomException;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import com.example.randomcoffee.model.db.repository.UserRepo;
import com.example.randomcoffee.model.enums.Role;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//@Service
//@RequiredArgsConstructor
//public class AuthenticationService {
//
//    private final UserRepo userRepo;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthenticationResponse register(RegisterRequest request) {
//
//        var user = CoffeeUser.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
//        userRepo.save(user);
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }
//
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//        String errorMsg = String.format("User with email %s not found");
//        CoffeeUser user = userRepo.findByEmail(request.getEmail()).orElseThrow(() -> new CustomException(errorMsg, HttpStatus.NOT_FOUND));
//        var jwtToken = jwtService.generateToken(user);
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }
//}
