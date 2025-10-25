package com.example.neo4j.domain;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tags", "categories", "referencedPosts", "author", "company"})
@EqualsAndHashCode(of = {"id"})
public class BlogPost {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String url;

    private LocalDateTime publishedAt;

    private String summary;

    @Relationship(type = "WRITTEN_BY", direction = Relationship.Direction.OUTGOING)
    private Author author;  // nullable

    @Relationship(type = "TAGGED_WITH", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();  // multiple tags

    @Relationship(type = "CATEGORIZED_AS", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Category> categories = new HashSet<>();  // multiple categories

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Company company;

    @Relationship(type = "REFERENCES", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<BlogPost> referencedPosts = new HashSet<>();
}
