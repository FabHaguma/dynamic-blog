package com.haguma.blog.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String markdownContent;
    
    @Column(name = "html_content", columnDefinition = "TEXT")
    private String htmlContent;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;

    private OffsetDateTime createdAt = OffsetDateTime.now();
    private OffsetDateTime publishedAt;

    private int likeCount = 0;
    private String templateId = "default";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    public enum PostStatus {
        DRAFT, PUBLISHED
    }
}