package com.haguma.blog.repository;

import com.haguma.blog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(UUID postId);
}