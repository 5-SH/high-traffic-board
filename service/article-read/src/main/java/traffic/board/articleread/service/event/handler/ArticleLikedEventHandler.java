package traffic.board.articleread.service.event.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import traffic.board.articleread.repository.ArticleQueryModelRepository;
import traffic.board.common.event.Event;
import traffic.board.common.event.EventType;
import traffic.board.common.event.payload.ArticleLikedEventPayload;
import traffic.board.common.event.payload.CommentCreatedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleLikedEventHandler implements EventHandler<ArticleLikedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleLikedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel -> {
                    articleQueryModel.updateBy(event.getPayload());
                    articleQueryModelRepository.update(articleQueryModel);
                });
    }

    @Override
    public boolean supports(Event<ArticleLikedEventPayload> event) {
        return EventType.ARTICLE_LINKED == event.getType();
    }
}
