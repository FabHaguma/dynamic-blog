package com.haguma.blog.controller;

import com.haguma.blog.dto.PostDetailDto;
import com.haguma.blog.dto.PostSummaryDto;
import com.haguma.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getAllPosts(page, size));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping
    public ResponseEntity<PostDetailDto> createPost(@RequestBody PostDetailDto postDto) {
        // In Iteration 1, creating a post only requires title and content
        return ResponseEntity.ok(postService.createPost(postDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(@PathVariable UUID id, @RequestBody PostDetailDto postDto) {
        return ResponseEntity.ok(postService.updatePost(id, postDto));
    }
}