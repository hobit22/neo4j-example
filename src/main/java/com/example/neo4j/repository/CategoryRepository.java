package com.example.neo4j.repository;

import com.example.neo4j.domain.Category;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends Neo4jRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query("MATCH (category:Category)<-[:CATEGORIZED_AS]-(post:BlogPost) " +
           "RETURN category, COUNT(post) as postCount " +
           "ORDER BY postCount DESC " +
           "LIMIT $limit")
    List<Category> findMostUsedCategories(@Param("limit") int limit);
}
