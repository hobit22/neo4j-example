package com.example.neo4j.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostRequest {

    private String title;
    private String url;
    private LocalDateTime publishedAt;
    private String summary;
    private String authorName;
    private Set<String> topicNames;
    private String companyName;
}
