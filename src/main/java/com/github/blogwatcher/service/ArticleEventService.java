package com.github.blogwatcher.service;

import com.github.blogwatcher.model.event.ArticleEvent;
import com.github.blogwatcher.model.event.ChangeType;
import com.github.blogwatcher.repository.ArticleEventRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ArticleEventService {
    private final ArticleEventRepository articleEventRepository;

    public ArticleEventService(ArticleEventRepository articleEventRepository) {
        this.articleEventRepository = articleEventRepository;
    }

    @CacheEvict(value = "pagedEvents", allEntries = true)
    public ArticleEvent create(ArticleEvent articleEvent) {
        return articleEventRepository.save(articleEvent);
    }

    @Cacheable(value = "pagedEvents", key = "#changeType + '_' + (#date != null ? #date.toString() : 'null') +" +
            " '_page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize")
    public Page<ArticleEvent> findEvents(String changeType, LocalDate date, Pageable pageable) {
        validateRequest(changeType);

        if (changeType != null && !changeType.isEmpty() && date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59);
            ChangeType change = ChangeType.valueOf(changeType);
            return articleEventRepository.findByChangeTypeAndCreatedAtBetween(change, start, end, pageable);
        } else if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atStartOfDay().withHour(23).withMinute(59).withSecond(59);
            return articleEventRepository.findByCreatedAtBetween(start, end, pageable);
        } else if (changeType != null && !changeType.isEmpty()) {
            ChangeType change = ChangeType.valueOf(changeType);
            return articleEventRepository.findByChangeType(change, pageable);
        }
        return articleEventRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        articleEventRepository.deleteById(id);
    }

    private void validateRequest(String changeType) {
        if (changeType != null && !changeType.isEmpty()) {
            try {
                ChangeType.valueOf(changeType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid change type: " + changeType);
            }
        }

    }
}
