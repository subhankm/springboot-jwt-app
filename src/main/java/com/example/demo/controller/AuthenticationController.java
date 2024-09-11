package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login( @RequestBody User request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    public String use(){
        return "Home---user";
    }
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin(){
        return "Home----admin";
    }
}