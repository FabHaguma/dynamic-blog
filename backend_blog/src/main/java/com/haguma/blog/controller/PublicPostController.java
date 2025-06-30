package com.haguma.blog.controller;

import com.haguma.blog.dto.CommentDto;
import com.haguma.blog.dto.PostDetailDto;
import com.haguma.blog.dto.PostSummaryDto;
import com.haguma.blog.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/posts")
@RequiredArgsConstructor
public class PublicPostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryDto>> getPublishedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(postService.getPublishedPosts(page, size));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PostDetailDto> getPostBySlug(@PathVariable String slug) {
        PostDetailDto post = postService.getPublishedPostBySlug(slug);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likePost(@PathVariable UUID id) {
        postService.addLike(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable UUID id, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(postService.addComment(id, commentDto));
    }
}