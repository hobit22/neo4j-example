package com.example.neo4j.service;

import com.example.neo4j.domain.Author;
import com.example.neo4j.domain.BlogPost;
import com.example.neo4j.domain.Company;
import com.example.neo4j.domain.Topic;
import com.example.neo4j.dto.BlogPostRequest;
import com.example.neo4j.repository.AuthorRepository;
import com.example.neo4j.repository.BlogPostRepository;
import com.example.neo4j.repository.CompanyRepository;
import com.example.neo4j.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final AuthorRepository authorRepository;
    private final TopicRepository topicRepository;
    private final CompanyRepository companyRepository;

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlogPost not found with id: " + id));
    }

    public List<BlogPost> searchByTitle(String title) {
        return blogPostRepository.findByTitleContaining(title);
    }

    public List<BlogPost> getPostsByCompany(String companyName) {
        return blogPostRepository.findByCompanyName(companyName);
    }

    public List<BlogPost> getPostsByTopic(String topicName) {
        return blogPostRepository.findByTopicName(topicName);
    }

    public List<BlogPost> getPostsByAuthor(String authorName) {
        return blogPostRepository.findByAuthorName(authorName);
    }

    public List<BlogPost> getRelatedPosts(Long postId, int limit) {
        return blogPostRepository.findRelatedPostsByTopics(postId, limit);
    }

    public List<BlogPost> getPostsByMultipleTopics(List<String> topicNames) {
        return blogPostRepository.findByMultipleTopics(topicNames);
    }

    @Transactional
    public BlogPost createPost(BlogPostRequest request) {
        // Find or create author (nullable)
        Author author = null;
        if (request.getAuthorName() != null && !request.getAuthorName().isEmpty()) {
            author = authorRepository.findByName(request.getAuthorName())
                    .orElseGet(() -> authorRepository.save(
                            Author.builder()
                                    .name(request.getAuthorName())
                                    .build()
                    ));
        }

        // Find or create topics (multiple)
        Set<Topic> topics = new HashSet<>();
        if (request.getTopicNames() != null) {
            for (String topicName : request.getTopicNames()) {
                Topic topic = topicRepository.findByName(topicName)
                        .orElseGet(() -> topicRepository.save(
                                Topic.builder()
                                        .name(topicName)
                                        .build()
                        ));
                topics.add(topic);
            }
        }

        // Find or create company
        Company company = null;
        if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
            company = companyRepository.findByName(request.getCompanyName())
                    .orElseGet(() -> companyRepository.save(
                            Company.builder()
                                    .name(request.getCompanyName())
                                    .build()
                    ));
        }

        // Create blog post
        BlogPost blogPost = BlogPost.builder()
                .title(request.getTitle())
                .url(request.getUrl())
                .publishedAt(request.getPublishedAt())
                .summary(request.getSummary())
                .author(author)
                .topics(topics)
                .company(company)
                .build();

        return blogPostRepository.save(blogPost);
    }

    @Transactional
    public BlogPost updatePost(Long id, BlogPostRequest request) {
        BlogPost existingPost = getPostById(id);

        existingPost.setTitle(request.getTitle());
        existingPost.setUrl(request.getUrl());
        existingPost.setPublishedAt(request.getPublishedAt());
        existingPost.setSummary(request.getSummary());

        // Update author
        if (request.getAuthorName() != null && !request.getAuthorName().isEmpty()) {
            Author author = authorRepository.findByName(request.getAuthorName())
                    .orElseGet(() -> authorRepository.save(
                            Author.builder()
                                    .name(request.getAuthorName())
                                    .build()
                    ));
            existingPost.setAuthor(author);
        } else {
            existingPost.setAuthor(null);
        }

        // Update topics
        Set<Topic> topics = new HashSet<>();
        if (request.getTopicNames() != null) {
            for (String topicName : request.getTopicNames()) {
                Topic topic = topicRepository.findByName(topicName)
                        .orElseGet(() -> topicRepository.save(
                                Topic.builder()
                                        .name(topicName)
                                        .build()
                        ));
                topics.add(topic);
            }
        }
        existingPost.setTopics(topics);

        // Update company
        if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
            Company company = companyRepository.findByName(request.getCompanyName())
                    .orElseGet(() -> companyRepository.save(
                            Company.builder()
                                    .name(request.getCompanyName())
                                    .build()
                    ));
            existingPost.setCompany(company);
        }

        return blogPostRepository.save(existingPost);
    }

    @Transactional
    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }
}
