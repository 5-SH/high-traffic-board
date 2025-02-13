package traffic.board.common.event;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import traffic.board.common.event.payload.ArticleCreatedEventPayload;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    @Test
    void serde() {
        ArticleCreatedEventPayload payload = ArticleCreatedEventPayload.builder()
                .articleId(1L)
                .title("title")
                .content("content")
                .boardId(1L)
                .writerId(1L)
                .createAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .boardArticleCount(23L)
                .build();

        Event<EventPayload> event = Event.of(
                1234L,
                EventType.ARTICLE_CREATED,
                payload
        );

        String json = event.toJson();
        System.out.println("json = " + json);

        // when
        Event<EventPayload> result = Event.fromJson(json);

        // then
        Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
        Assertions.assertThat(result.getType()).isEqualTo(event.getType());
        Assertions.assertThat(result.getPayload()).isInstanceOf(payload.getClass());

        ArticleCreatedEventPayload resultPayload = (ArticleCreatedEventPayload) result.getPayload();
        Assertions.assertThat(resultPayload.getArticleId()).isEqualTo(payload.getArticleId());
        Assertions.assertThat(resultPayload.getTitle()).isEqualTo(payload.getTitle());
        Assertions.assertThat(resultPayload.getCreateAt()).isEqualTo(payload.getCreateAt());
    }

}