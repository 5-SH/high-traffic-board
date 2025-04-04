package traffic.board.common.event.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import traffic.board.common.event.EventPayload;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLikedEventPayload implements EventPayload {
    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;
    private Long articleLikeCount;
}
