package com.haguma.blog.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TagDto {
    private UUID id;
    private String name;
    private String slug;
}