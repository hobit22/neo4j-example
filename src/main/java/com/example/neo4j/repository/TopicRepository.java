package com.example.neo4j.repository;

import com.example.neo4j.domain.Topic;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends Neo4jRepository<Topic, Long> {

    Optional<Topic> findByName(String name);

    List<Topic> findByCategory(String category);

    @Query("MATCH (topic:Topic)-[:RELATED_TO]->(related:Topic) " +
           "WHERE topic.name = $topicName " +
           "RETURN related")
    List<Topic> findRelatedTopics(@Param("topicName") String topicName);

    @Query("MATCH (topic:Topic)<-[:TAGGED_WITH]-(post:BlogPost) " +
           "RETURN topic, COUNT(post) as postCount " +
           "ORDER BY postCount DESC " +
           "LIMIT $limit")
    List<Topic> findMostUsedTopics(@Param("limit") int limit);
}
