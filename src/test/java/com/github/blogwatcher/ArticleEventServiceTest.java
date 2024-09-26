package com.github.blogwatcher;

import com.github.blogwatcher.model.event.ArticleEvent;
import com.github.blogwatcher.model.event.ChangeType;
import com.github.blogwatcher.model.event.Changes;
import com.github.blogwatcher.service.ArticleEventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class ArticleEventServiceTest {

	@Container
	@ServiceConnection
	static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0");

	@Autowired
	private ArticleEventService articleEventService;

	@BeforeEach
	void setUp() {
		List<Long> ids = List.of(1L, 2L, 3L, 4L);
		for (Long id : ids) {
			var articleEvent = new ArticleEvent();
			if (id == 4L) {
				articleEvent = getNewArticleEvent(id, ChangeType.UPDATED, LocalDateTime.now());
				articleEventService.create(articleEvent);
			} else {
				articleEvent = getNewArticleEvent(id, ChangeType.CREATED, LocalDateTime.now());
				articleEventService.create(articleEvent);
			}

		}
	}

	@AfterEach
	void cleanup() {
		List<Long> ids = List.of(1L, 2L, 3L, 4L);
		for (Long id : ids) {
			articleEventService.deleteById(id);
		}
	}

	@Test
	void createNewArticleEvent() {
		var createdTime = LocalDateTime.now();
		var newArticleEvent = getNewArticleEvent(5L, ChangeType.UPDATED, createdTime);
		var created = articleEventService.create(newArticleEvent);
		assertThat(created).isNotNull();
		assertThat(created.getCreatedAt()).isEqualTo(createdTime);
		assertThat(created.getArticleId()).isEqualTo(5L);
		assertThat(created.getAuthorId()).isEqualTo(5L);

	}


	@Test
	void findAllArticleEvents() {
		Pageable pageable = PageRequest.of(0, 5,  Sort.by(Sort.Direction.ASC, "createdAt"));
		var result = articleEventService.findEvents(null, null, pageable);

		assertThat(result).isNotNull();
		var listEvents = result.getContent();
		assertThat(listEvents.size()).isEqualTo(4);

		var first = listEvents.get(0);
		assertThat(first.getArticleId()).isEqualTo(1L);
		assertThat(first.getChangeType()).isEqualTo(ChangeType.CREATED);

		var second = listEvents.get(1);
		assertThat(second.getArticleId()).isEqualTo(2L);
		assertThat(second.getChangeType()).isEqualTo(ChangeType.CREATED);

		var third = listEvents.get(2);
		assertThat(third.getArticleId()).isEqualTo(3L);
		assertThat(third.getChangeType()).isEqualTo(ChangeType.CREATED);

		var fourth = listEvents.get(3);
		assertThat(fourth.getArticleId()).isEqualTo(4L);
		assertThat(fourth.getChangeType()).isEqualTo(ChangeType.UPDATED);

	}

	@Test
	void findAllUpdatedArticleEvents() {

		Pageable pageable = PageRequest.of(0, 5);
		var result = articleEventService.findEvents("UPDATED", null, pageable);

		assertThat(result).isNotNull();
		var listEvents = result.getContent();
		assertThat(listEvents.size()).isEqualTo(1);

		var first = listEvents.get(0);
		assertThat(first.getArticleId()).isEqualTo(4L);
		assertThat(first.getChangeType()).isEqualTo(ChangeType.UPDATED);

	}

	@Test
	void findAllCreatedArticleEvents() {

		Pageable pageable = PageRequest.of(0, 5);
		var result = articleEventService.findEvents("CREATED", null, pageable);

		assertThat(result).isNotNull();
		var listEvents = result.getContent();
		assertThat(listEvents.size()).isEqualTo(3);

		var first = listEvents.get(0);
		assertThat(first.getArticleId()).isEqualTo(1L);
		assertThat(first.getChangeType()).isEqualTo(ChangeType.CREATED);

		var second = listEvents.get(1);
		assertThat(second.getArticleId()).isEqualTo(2L);
		assertThat(second.getChangeType()).isEqualTo(ChangeType.CREATED);

		var third = listEvents.get(2);
		assertThat(third.getArticleId()).isEqualTo(3L);
		assertThat(third.getChangeType()).isEqualTo(ChangeType.CREATED);

	}


	@Test
	void findAllTodaysArticleEvents() {

		Pageable pageable = PageRequest.of(0, 5);
		var result = articleEventService.findEvents(null, LocalDate.now(), pageable);

		assertThat(result).isNotNull();
		var listEvents = result.getContent();
		assertThat(listEvents.size()).isEqualTo(4);

		var first = listEvents.get(0);
		assertThat(first.getArticleId()).isEqualTo(1L);
		assertThat(first.getChangeType()).isEqualTo(ChangeType.CREATED);

		var second = listEvents.get(1);
		assertThat(second.getArticleId()).isEqualTo(2L);
		assertThat(second.getChangeType()).isEqualTo(ChangeType.CREATED);

		var third = listEvents.get(2);
		assertThat(third.getArticleId()).isEqualTo(3L);
		assertThat(third.getChangeType()).isEqualTo(ChangeType.CREATED);

		var fourth = listEvents.get(3);
		assertThat(fourth.getArticleId()).isEqualTo(4L);
		assertThat(fourth.getChangeType()).isEqualTo(ChangeType.UPDATED);

	}

	@Test
	void findAllYesterdaysArticleEvents() {

		Pageable pageable = PageRequest.of(0, 5);
		var result = articleEventService.findEvents(null, LocalDate.now().minusDays(1L), pageable);

		assertThat(result).isNotNull();
		var listEvents = result.getContent();
		assertThat(listEvents.size()).isEqualTo(0);


	}

	private ArticleEvent getNewArticleEvent(Long id, ChangeType type, LocalDateTime dateTime) {
		return new ArticleEvent()
				.setId(id)
				.setArticleId(id)
				.setAuthorId(id)
				.setChangeType(type)
				.setCreatedAt(dateTime)
				.setChanges(List.of(new Changes("title", "description")));
	}

}
