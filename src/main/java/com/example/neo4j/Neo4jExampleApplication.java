package com.example.neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories
public class Neo4jExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(Neo4jExampleApplication.class, args);
    }
}
