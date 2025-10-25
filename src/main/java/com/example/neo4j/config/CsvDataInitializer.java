package com.example.neo4j.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CsvDataInitializer {

    private final Driver driver;

    @Bean
    @Order(1)
    public CommandLineRunner loadCsvData() {
        return args -> {
            log.info("Loading data from CSV files...");

            try (Session session = driver.session()) {
                // Clear existing data
                log.info("Clearing existing data...");
                session.run("MATCH (n) DETACH DELETE n");

                // Load Companies
                log.info("Loading companies...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///blogs.csv' AS row " +
                    "CREATE (c:Company {" +
                    "  name: row.company, " +
                    "  blogUrl: row.site_url" +
                    "})"
                );

                // Load Tags
                log.info("Loading tags...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///tags.csv' AS row " +
                    "CREATE (t:Tag {" +
                    "  originalId: toInteger(row.id), " +
                    "  name: row.name, " +
                    "  description: row.coalesce, " +
                    "  tagGroup: row.coalesce" +
                    "})"
                );

                // Load Categories
                log.info("Loading categories...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///categories.csv' AS row " +
                    "CREATE (cat:Category {" +
                    "  originalId: toInteger(row.id), " +
                    "  name: row.name, " +
                    "  description: row.coalesce" +
                    "})"
                );

                // Create blog ID to company mapping first
                log.info("Creating blog ID index...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///blogs.csv' AS row " +
                    "MATCH (c:Company {name: row.company}) " +
                    "SET c.blogId = toInteger(row.id)"
                );

                // Load Posts with relationships
                log.info("Loading posts...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///posts.csv' AS row " +
                    "MATCH (c:Company {blogId: toInteger(row.blog_id)}) " +
                    "CREATE (p:BlogPost {" +
                    "  originalId: toInteger(row.id), " +
                    "  title: row.title, " +
                    "  url: row.original_url, " +
                    "  summary: row.summary_content, " +
                    "  publishedAt: datetime(replace(row.published_at, ' ', 'T'))" +
                    "}) " +
                    "CREATE (p)-[:BELONGS_TO]->(c) " +
                    "WITH p, row " +
                    "WHERE row.author IS NOT NULL AND row.author <> '' " +
                    "MERGE (a:Author {name: row.author}) " +
                    "CREATE (p)-[:WRITTEN_BY]->(a)"
                );

                // Link posts with tags
                log.info("Creating post-tag relationships...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///post_tags.csv' AS row " +
                    "MATCH (p:BlogPost {originalId: toInteger(row.post_id)}) " +
                    "MATCH (t:Tag {originalId: toInteger(row.tag_id)}) " +
                    "CREATE (p)-[:TAGGED_WITH]->(t)"
                );

                // Link posts with categories
                log.info("Creating post-category relationships...");
                session.run(
                    "LOAD CSV WITH HEADERS FROM 'file:///post_categories.csv' AS row " +
                    "MATCH (p:BlogPost {originalId: toInteger(row.post_id)}) " +
                    "MATCH (cat:Category {originalId: toInteger(row.category_id)}) " +
                    "CREATE (p)-[:CATEGORIZED_AS]->(cat)"
                );

                // Get counts
                long companyCount = session.run("MATCH (c:Company) RETURN count(c) as count")
                    .single().get("count").asLong();
                long tagCount = session.run("MATCH (t:Tag) RETURN count(t) as count")
                    .single().get("count").asLong();
                long categoryCount = session.run("MATCH (cat:Category) RETURN count(cat) as count")
                    .single().get("count").asLong();
                long postCount = session.run("MATCH (p:BlogPost) RETURN count(p) as count")
                    .single().get("count").asLong();
                long authorCount = session.run("MATCH (a:Author) RETURN count(a) as count")
                    .single().get("count").asLong();

                log.info("CSV data loaded successfully!");
                log.info("Imported {} companies", companyCount);
                log.info("Imported {} tags", tagCount);
                log.info("Imported {} categories", categoryCount);
                log.info("Imported {} authors", authorCount);
                log.info("Imported {} blog posts", postCount);
            } catch (Exception e) {
                log.error("Error loading CSV data", e);
            }
        };
    }
}
