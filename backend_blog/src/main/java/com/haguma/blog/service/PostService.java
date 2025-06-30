// backend_blog/src/main/java/com/haguma/blog/service/PostService.java
package com.haguma.blog.service;

import com.github.slugify.Slugify;
import com.haguma.blog.dto.CommentDto;
import com.haguma.blog.dto.PostDetailDto;
import com.haguma.blog.dto.PostSummaryDto;
import com.haguma.blog.entity.*;
import com.haguma.blog.entity.Post.PostStatus;
import com.haguma.blog.repository.CategoryRepository;
import com.haguma.blog.repository.CommentRepository;
import com.haguma.blog.repository.PostRepository;
import com.haguma.blog.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;
    
    private final Parser markdownParser = Parser.builder().build();
    private final HtmlRenderer htmlRenderer = HtmlRenderer.builder().build();
    private final Slugify slugify = Slugify.builder().build();

    // --- Public Methods ---

    @Transactional(readOnly = true)
    public Page<PostSummaryDto> getPublishedPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return postRepository.findByStatus(PostStatus.PUBLISHED, pageRequest).map(this::convertToSummaryDto);
    }
    
    @Transactional(readOnly = true)
    public Page<PostSummaryDto> getPublishedPostsByTag(String slug, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("publishedAt").descending());
        return postRepository.findByTags_SlugAndStatus(slug, PostStatus.PUBLISHED, pageRequest).map(this::convertToSummaryDto);
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPublishedPostBySlug(String slug) {
        return postRepository.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
                .map(this::convertToDetailDto)
                .orElse(null);
    }

    // --- Admin Methods ---

    @Transactional(readOnly = true)
    public Page<PostSummaryDto> getAllPosts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAll(pageRequest).map(this::convertToSummaryDto);
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostById(UUID id) {
        return postRepository.findById(id)
            .map(this::convertToDetailDto)
            .orElse(null);
    }

    @Transactional
    public PostDetailDto createPost(PostDetailDto postDto) {
        Post post = new Post();
        updatePostFromDto(post, postDto);
        post.setStatus(PostStatus.DRAFT);
        Post savedPost = postRepository.save(post);
        return convertToDetailDto(savedPost);
    }

    @Transactional
    public PostDetailDto updatePost(UUID id, PostDetailDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        updatePostFromDto(post, postDto);
        if (postDto.getStatus() == PostStatus.PUBLISHED && post.getStatus() == PostStatus.DRAFT) {
            post.setPublishedAt(OffsetDateTime.now());
        }
        post.setStatus(postDto.getStatus());
        Post updatedPost = postRepository.save(post);
        return convertToDetailDto(updatedPost);
    }
    
    // --- Engagement Methods ---

    @Transactional
    public void addLike(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(UUID postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
                .stream().map(this::convertCommentToDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto addComment(UUID postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthorName(commentDto.getAuthorName() == null || commentDto.getAuthorName().isBlank() ? "Anonymous" : commentDto.getAuthorName());
        comment.setContent(commentDto.getContent());
        Comment savedComment = commentRepository.save(comment);
        return convertCommentToDto(savedComment);
    }

    // --- Helper & Converter Methods ---

    private void updatePostFromDto(Post post, PostDetailDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new IllegalArgumentException("Post title cannot be empty.");
        }
        String newTitle = dto.getTitle().trim();
        if (post.getSlug() == null || !newTitle.equals(post.getTitle())) {
            post.setSlug(slugify.slugify(newTitle));
        }
        post.setTitle(newTitle);
        post.setMarkdownContent(dto.getMarkdownContent() != null ? dto.getMarkdownContent() : "");
        Node document = markdownParser.parse(post.getMarkdownContent());
        post.setHtmlContent(htmlRenderer.render(document));

        // Handle Category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        // Handle Tags
        Set<Tag> tags = new HashSet<>();
        if (dto.getTags() != null) {
            for (String tagName : dto.getTags()) {
                Tag tag = tagRepository.findByName(tagName.trim())
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName.trim());
                        newTag.setSlug(slugify.slugify(tagName.trim()));
                        return tagRepository.save(newTag);
                    });
                tags.add(tag);
            }
        }
        post.setTags(tags);
        post.setTemplateId(dto.getTemplateId() != null ? dto.getTemplateId() : "default");
    }

    private PostSummaryDto convertToSummaryDto(Post post) {
        PostSummaryDto dto = new PostSummaryDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSlug(post.getSlug());
        dto.setStatus(post.getStatus());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setPublishedAt(post.getPublishedAt());
        if (post.getCategory() != null) {
            dto.setCategoryName(post.getCategory().getName());
        }
        if (post.getTags() != null) {
            dto.setTagNames(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        }
        return dto;
    }

    private PostDetailDto convertToDetailDto(Post post) {
        PostDetailDto dto = new PostDetailDto();
        // ... copy existing fields
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setSlug(post.getSlug());
        dto.setMarkdownContent(post.getMarkdownContent());
        dto.setHtmlContent(post.getHtmlContent());
        dto.setStatus(post.getStatus());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setPublishedAt(post.getPublishedAt());
        // --- Set new fields ---
        dto.setLikeCount(post.getLikeCount());
        if (post.getCategory() != null) {
            dto.setCategoryId(post.getCategory().getId());
            dto.setCategoryName(post.getCategory().getName());
        }
        if (post.getTags() != null) {
            dto.setTags(post.getTags().stream().map(Tag::getName).collect(Collectors.toSet()));
        }
        dto.setTemplateId(post.getTemplateId());
        return dto;
    }
    
    private CommentDto convertCommentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setAuthorName(comment.getAuthorName());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}