package traffic.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import traffic.board.common.event.Event;
import traffic.board.common.event.EventPayload;
import traffic.board.common.event.EventType;
import traffic.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(
            topics = {
                EventType.Topic.TRAFFIC_BOARD_ARTICLE,
                EventType.Topic.TRAFFIC_BOARD_COMMENT,
                EventType.Topic.TRAFFIC_BOARD_LIKE,
                EventType.Topic.TRAFFIC_BOARD_VIEW
            },
            groupId = "traffic-board-hot-article-service"
        )
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
