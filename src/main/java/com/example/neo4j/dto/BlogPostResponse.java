package com.example.neo4j.dto;

import com.example.neo4j.domain.BlogPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostResponse {

    private Long id;
    private String title;
    private String url;
    private LocalDateTime publishedAt;
    private String summary;
    private String authorName;
    private Set<String> topicNames;
    private String companyName;

    public static BlogPostResponse from(BlogPost blogPost) {
        return BlogPostResponse.builder()
                .id(blogPost.getId())
                .title(blogPost.getTitle())
                .url(blogPost.getUrl())
                .publishedAt(blogPost.getPublishedAt())
                .summary(blogPost.getSummary())
                .authorName(blogPost.getAuthor() != null ? blogPost.getAuthor().getName() : null)
                .topicNames(blogPost.getTopics() != null ?
                        blogPost.getTopics().stream()
                                .map(topic -> topic.getName())
                                .collect(Collectors.toSet()) : Set.of())
                .companyName(blogPost.getCompany() != null ? blogPost.getCompany().getName() : null)
                .build();
    }
}
