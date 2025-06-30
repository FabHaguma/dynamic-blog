package com.haguma.blog.repository;

import com.haguma.blog.entity.Post;
import com.haguma.blog.entity.Post.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
    Optional<Post> findBySlugAndStatus(String slug, PostStatus status);
    Page<Post> findByTags_SlugAndStatus(String slug, PostStatus status, Pageable pageable);
}