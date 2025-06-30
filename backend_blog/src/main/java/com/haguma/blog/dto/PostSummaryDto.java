package com.haguma.blog.dto;

import com.haguma.blog.entity.Post.PostStatus;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class PostSummaryDto {
    private UUID id;
    private String title;
    private String slug;
    private PostStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime publishedAt;
    private String categoryName;
    private Set<String> tagNames;
}