package com.github.blogwatcher.service.listener;

import com.github.blogwatcher.service.ArticleEventConverter;
import com.github.blogwatcher.service.ArticleEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArticleEventListener implements EventListener {

    private final ArticleEventService articleEventService;
    public ArticleEventListener(ArticleEventService articleEventService) {
        this.articleEventService = articleEventService;
    }
    @Override
    public void processMessage(String message) {
        try {
            var articleEvent = ArticleEventConverter.convertToArticleEvent(message);
            articleEventService.create(articleEvent);
        } catch (Exception e) {
            log.error("Error while processing event {}",  message, e);
        }
    }
}
