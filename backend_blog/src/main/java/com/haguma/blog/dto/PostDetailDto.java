package com.haguma.blog.dto;

import com.haguma.blog.entity.Post.PostStatus;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class PostDetailDto {
    private UUID id;
    private String title;
    private String slug;
    private String markdownContent;
    private String htmlContent;
    private PostStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime publishedAt;
    private int likeCount;
    private UUID categoryId;
    private String categoryName; // For display
    private Set<String> tags; // We'll work with tag names (strings) for simplicity
    private String templateId;
}