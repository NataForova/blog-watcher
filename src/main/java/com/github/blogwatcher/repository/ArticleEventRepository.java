package com.github.blogwatcher.repository;

import com.github.blogwatcher.model.event.ArticleEvent;
import com.github.blogwatcher.model.event.ChangeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface ArticleEventRepository extends MongoRepository<ArticleEvent, Long> {
    Page<ArticleEvent> findByChangeType(ChangeType changeType, Pageable pageable);
    Page<ArticleEvent> findByChangeTypeAndCreatedAtBetween(ChangeType changeType,
                                                           LocalDateTime from,
                                                           LocalDateTime to,
                                                           Pageable pageable);
    Page<ArticleEvent> findByCreatedAtBetween(LocalDateTime from,
                                              LocalDateTime to,
                                              Pageable pageable);

}
