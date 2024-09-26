package com.github.blogwatcher.service;

import com.github.blogwatcher.service.listener.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaConsumerService {
    private List<EventListener> messageListeners = new ArrayList<>();

    public void addListener(EventListener listener) {
        messageListeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        messageListeners.remove(listener);
    }

    @KafkaListener(topics = "${kafka.topic.name}",
            groupId = "${spring.kafka.consumer.group-id}")
    public void listenGroupFoo(String message) {
        log.info("Received Message: {}", message);
        for (EventListener listener : messageListeners) {
            listener.processMessage(message);
        }
    }
}