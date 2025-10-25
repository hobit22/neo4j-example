package com.example.neo4j.repository;

import com.example.neo4j.domain.Author;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends Neo4jRepository<Author, Long> {

    Optional<Author> findByName(String name);
}
