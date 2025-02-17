package traffic.board.articleread.service.event.handler;

import traffic.board.common.event.Event;
import traffic.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload>  {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
