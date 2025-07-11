package com.haguma.blog.controller;

import com.haguma.blog.dto.AuthRequestDto;
import com.haguma.blog.dto.AuthResponseDto;
import com.haguma.blog.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto request) {
        String token = authService.authenticate(request);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}