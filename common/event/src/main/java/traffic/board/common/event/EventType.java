package traffic.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import traffic.board.common.event.payload.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.TRAFFIC_BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleDeletedEventPayload.class, Topic.TRAFFIC_BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.TRAFFIC_BOARD_ARTICLE),
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.TRAFFIC_BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.TRAFFIC_BOARD_COMMENT),
    ARTICLE_LINKED(ArticleLikedEventPayload.class, Topic.TRAFFIC_BOARD_LIKE),
    ARTICLE_UNLINKED(ArticleUnlikedEventPayload.class, Topic.TRAFFIC_BOARD_LIKE),
    ARTICLE_VIEWED(ArticleViewEventPayload.class, Topic.TRAFFIC_BOARD_VIEW);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventTye.from] type={}" , type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String TRAFFIC_BOARD_ARTICLE = "traffic-board-article";
        public static final String TRAFFIC_BOARD_COMMENT = "traffic-board-comment";
        public static final String TRAFFIC_BOARD_LIKE = "traffic-board-like";
        public static final String TRAFFIC_BOARD_VIEW = "traffic-board-view";
    }
}
