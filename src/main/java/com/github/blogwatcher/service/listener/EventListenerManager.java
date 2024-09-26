package com.github.blogwatcher.service.listener;

import com.github.blogwatcher.service.KafkaConsumerService;
import org.springframework.stereotype.Service;

@Service
public class EventListenerManager {
    private final KafkaConsumerService kafkaConsumerService;
    private final ArticleEventListener articleEventListener;
    public EventListenerManager(KafkaConsumerService kafkaConsumerService,
                                ArticleEventListener articleEventListener) {
        this.kafkaConsumerService = kafkaConsumerService;
        this.articleEventListener = articleEventListener;
        kafkaConsumerService.addListener(articleEventListener);
    }
}
