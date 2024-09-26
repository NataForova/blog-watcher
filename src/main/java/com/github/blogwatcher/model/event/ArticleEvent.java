package com.github.blogwatcher.model.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document("article_event")
@Schema(description = "Details about an article event")
public class ArticleEvent {
    @Id
    private Long id;
    private Long articleId;
    private Long authorId;
    private ChangeType changeType;
    private List<Changes> changes;
    private LocalDateTime createdAt;
}
