package com.haguma.blog.controller;

import com.haguma.blog.dto.PostSummaryDto;
import com.haguma.blog.dto.TagDto;
import com.haguma.blog.service.PostService;
import com.haguma.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final PostService postService;
    
    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{slug}/posts")
    public ResponseEntity<Page<PostSummaryDto>> getPostsByTag(
        @PathVariable String slug,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.getPublishedPostsByTag(slug, page, size));
    }
}