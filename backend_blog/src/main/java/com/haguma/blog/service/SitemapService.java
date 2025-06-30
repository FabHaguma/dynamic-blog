package com.haguma.blog.service;

import com.haguma.blog.entity.Post;
import com.haguma.blog.entity.Post.PostStatus;
import com.haguma.blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SitemapService {

    private final PostRepository postRepository;
    // You should move this to a configuration file in a real production app
    private final String baseUrl = "http://localhost:5173";

    @Transactional(readOnly = true)
    public String generateSitemap() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        List<Post> posts = postRepository.findByStatus(PostStatus.PUBLISHED, null).getContent();

        for (Post post : posts) {
            sb.append("  <url>\n");
            sb.append("    <loc>").append(baseUrl).append("/posts/").append(post.getSlug()).append("</loc>\n");
            if (post.getPublishedAt() != null) {
                sb.append("    <lastmod>").append(post.getPublishedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)).append("</lastmod>\n");
            }
            sb.append("  </url>\n");
        }

        sb.append("</urlset>");
        return sb.toString();
    }
}