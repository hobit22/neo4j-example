package com.example.neo4j.controller;

import com.example.neo4j.domain.Topic;
import com.example.neo4j.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<Topic>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Topic> getTopicById(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Topic> getTopicByName(@PathVariable String name) {
        return ResponseEntity.ok(topicService.getTopicByName(name));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Topic>> getTopicsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(topicService.getTopicsByCategory(category));
    }

    @GetMapping("/{name}/related")
    public ResponseEntity<List<Topic>> getRelatedTopics(@PathVariable String name) {
        return ResponseEntity.ok(topicService.getRelatedTopics(name));
    }

    @GetMapping("/most-used")
    public ResponseEntity<List<Topic>> getMostUsedTopics(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(topicService.getMostUsedTopics(limit));
    }

    @PostMapping
    public ResponseEntity<Topic> createTopic(@RequestBody Topic topic) {
        return ResponseEntity.status(HttpStatus.CREATED).body(topicService.createTopic(topic));
    }
}
