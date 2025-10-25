package com.example.neo4j.repository;

import com.example.neo4j.domain.BlogPost;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends Neo4jRepository<BlogPost, Long> {

    List<BlogPost> findByTitleContaining(String title);

    @Query("MATCH (post:BlogPost)-[:BELONGS_TO]->(company:Company) " +
           "WHERE company.name = $companyName " +
           "RETURN post")
    List<BlogPost> findByCompanyName(@Param("companyName") String companyName);

    @Query("MATCH (post:BlogPost)-[:TAGGED_WITH]->(topic:Topic) " +
           "WHERE topic.name = $topicName " +
           "RETURN post " +
           "ORDER BY post.publishedAt DESC")
    List<BlogPost> findByTopicName(@Param("topicName") String topicName);

    @Query("MATCH (post:BlogPost)-[:WRITTEN_BY]->(author:Author) " +
           "WHERE author.name = $authorName " +
           "RETURN post " +
           "ORDER BY post.publishedAt DESC")
    List<BlogPost> findByAuthorName(@Param("authorName") String authorName);

    @Query("MATCH (post:BlogPost {id: $postId})-[:TAGGED_WITH]->(topic:Topic)<-[:TAGGED_WITH]-(related:BlogPost) " +
           "WHERE post.id <> related.id " +
           "RETURN related, COUNT(topic) as commonTopics " +
           "ORDER BY commonTopics DESC " +
           "LIMIT $limit")
    List<BlogPost> findRelatedPostsByTopics(@Param("postId") Long postId, @Param("limit") int limit);

    @Query("MATCH (post:BlogPost)-[:TAGGED_WITH]->(topic:Topic) " +
           "WHERE topic.name IN $topicNames " +
           "RETURN post, COUNT(topic) as matchCount " +
           "ORDER BY matchCount DESC, post.publishedAt DESC")
    List<BlogPost> findByMultipleTopics(@Param("topicNames") List<String> topicNames);
}
