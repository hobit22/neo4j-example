package com.example.neo4j.controller;

import com.example.neo4j.domain.BlogPost;
import com.example.neo4j.dto.BlogPostRequest;
import com.example.neo4j.dto.BlogPostResponse;
import com.example.neo4j.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    public ResponseEntity<List<BlogPostResponse>> getAllPosts() {
        List<BlogPostResponse> posts = blogPostService.getAllPosts().stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> getPostById(@PathVariable Long id) {
        BlogPost post = blogPostService.getPostById(id);
        return ResponseEntity.ok(BlogPostResponse.from(post));
    }

    @GetMapping("/search")
    public ResponseEntity<List<BlogPostResponse>> searchByTitle(@RequestParam String title) {
        List<BlogPostResponse> posts = blogPostService.searchByTitle(title).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/company/{companyName}")
    public ResponseEntity<List<BlogPostResponse>> getPostsByCompany(@PathVariable String companyName) {
        List<BlogPostResponse> posts = blogPostService.getPostsByCompany(companyName).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/topic/{topicName}")
    public ResponseEntity<List<BlogPostResponse>> getPostsByTopic(@PathVariable String topicName) {
        List<BlogPostResponse> posts = blogPostService.getPostsByTopic(topicName).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<BlogPostResponse>> getPostsByAuthor(@PathVariable String authorName) {
        List<BlogPostResponse> posts = blogPostService.getPostsByAuthor(authorName).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<List<BlogPostResponse>> getRelatedPosts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit) {
        List<BlogPostResponse> posts = blogPostService.getRelatedPosts(id, limit).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/topics")
    public ResponseEntity<List<BlogPostResponse>> getPostsByMultipleTopics(@RequestBody List<String> topicNames) {
        List<BlogPostResponse> posts = blogPostService.getPostsByMultipleTopics(topicNames).stream()
                .map(BlogPostResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(posts);
    }

    @PostMapping
    public ResponseEntity<BlogPostResponse> createPost(@RequestBody BlogPostRequest request) {
        BlogPost post = blogPostService.createPost(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(BlogPostResponse.from(post));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostResponse> updatePost(
            @PathVariable Long id,
            @RequestBody BlogPostRequest request) {
        BlogPost post = blogPostService.updatePost(id, request);
        return ResponseEntity.ok(BlogPostResponse.from(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
