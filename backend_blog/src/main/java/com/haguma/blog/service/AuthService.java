package com.haguma.blog.service;

import com.haguma.blog.dto.AuthRequestDto;
import com.haguma.blog.repository.UserRepository;
import com.haguma.blog.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationProvider authenticationProvider;

    public String authenticate(AuthRequestDto request) {
        authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // Should be present after successful auth
        return jwtService.generateToken(user);
    }
}