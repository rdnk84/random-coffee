package com.example.randomcoffee.controllers;

import com.example.randomcoffee.config.auth.AuthenticationRequest;
import com.example.randomcoffee.config.auth.AuthenticationResponse;
import com.example.randomcoffee.config.auth.AuthenticationService;
import com.example.randomcoffee.config.auth.RegisterRequest;
import com.example.randomcoffee.model.db.entity.CoffeeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody CoffeeUser request) {
        return ResponseEntity.ok(service.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
