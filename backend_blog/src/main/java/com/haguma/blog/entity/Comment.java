package com.haguma.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String authorName;
    private String content;
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}