package com.example.neo4j.service;

import com.example.neo4j.domain.Topic;
import com.example.neo4j.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic getTopicById(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Topic not found with id: " + id));
    }

    public Topic getTopicByName(String name) {
        return topicRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Topic not found with name: " + name));
    }

    public List<Topic> getTopicsByCategory(String category) {
        return topicRepository.findByCategory(category);
    }

    public List<Topic> getRelatedTopics(String topicName) {
        return topicRepository.findRelatedTopics(topicName);
    }

    public List<Topic> getMostUsedTopics(int limit) {
        return topicRepository.findMostUsedTopics(limit);
    }

    @Transactional
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }
}
