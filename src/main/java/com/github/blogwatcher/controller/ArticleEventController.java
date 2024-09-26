package com.github.blogwatcher.controller;

import com.github.blogwatcher.model.event.ArticleEvent;
import com.github.blogwatcher.service.ArticleEventService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;

import java.time.LocalDate;

@RestController
@RequestMapping("/events")
@Tag(name = "Article events controller", description = "API for getting article's event")
public class ArticleEventController {
    private final ArticleEventService articleEventService;

    public ArticleEventController(ArticleEventService articleEventService) {
        this.articleEventService = articleEventService;
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved page of",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ArticleEvent.class))
                    )
            ),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<ArticleEvent>> getAllArticleEvents(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "100") int size,
                                                                  @RequestParam(required = false) String type,
                                                                  @RequestParam(required = false) LocalDate date) {
        Pageable pageable = PageRequest.of(page, size);
        var articlePage = articleEventService.findEvents(type, date, pageable);
        return ResponseEntity.ok(articlePage);
    }
}
