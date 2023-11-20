package com.example.randomcoffee.controllers;

import com.example.randomcoffee.config.auth.AuthenticationRequest;
import com.example.randomcoffee.config.auth.AuthenticationResponse;
import com.example.randomcoffee.config.auth.AuthenticationService;
import com.example.randomcoffee.config.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService service;

//    @GetMapping("/ololo")
//    public ResponseEntity<Object> register() {
////        throw new RuntimeException();
//        return ResponseEntity.ok("olololo 2");
//    }



//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(service.register(request));
//    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return "reg-form";
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
