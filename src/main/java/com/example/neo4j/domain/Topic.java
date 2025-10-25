package com.example.neo4j.domain;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"relatedTopics"})
@EqualsAndHashCode(of = {"id"})
public class Topic {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private String category;

    @Relationship(type = "RELATED_TO", direction = Relationship.Direction.OUTGOING)
    @Builder.Default
    private Set<Topic> relatedTopics = new HashSet<>();
}
