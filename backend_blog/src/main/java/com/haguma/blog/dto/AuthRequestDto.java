package com.haguma.blog.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String email;
    private String password;
}