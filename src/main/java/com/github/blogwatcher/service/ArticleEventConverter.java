package com.github.blogwatcher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.blogwatcher.model.event.ArticleEvent;
import org.springframework.stereotype.Service;

@Service
public class ArticleEventConverter {

    private final static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static ArticleEvent convertToArticleEvent(String jsonMessage) {
        if (jsonMessage == null) {
            throw new IllegalArgumentException("jsonMessage cannot be null");
        }
        try {
            return objectMapper.readValue(jsonMessage, ArticleEvent.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("jsonMessage is not a valid JSON object", e);
        }
    }

}
