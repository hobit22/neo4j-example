package com.example.neo4j.repository;

import com.example.neo4j.domain.Company;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends Neo4jRepository<Company, Long> {

    Optional<Company> findByName(String name);
}
