package com.haguma.blog.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CommentDto {
    private UUID id;
    private String authorName;
    private String content;
    private OffsetDateTime createdAt;
}