package com.example.neo4j.repository;

import com.example.neo4j.domain.Tag;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    List<Tag> findByTagGroup(String tagGroup);

    @Query("MATCH (tag:Tag)-[:RELATED_TO]->(related:Tag) " +
           "WHERE tag.name = $tagName " +
           "RETURN related")
    List<Tag> findRelatedTags(@Param("tagName") String tagName);

    @Query("MATCH (tag:Tag)<-[:TAGGED_WITH]-(post:BlogPost) " +
           "RETURN tag, COUNT(post) as postCount " +
           "ORDER BY postCount DESC " +
           "LIMIT $limit")
    List<Tag> findMostUsedTags(@Param("limit") int limit);
}
