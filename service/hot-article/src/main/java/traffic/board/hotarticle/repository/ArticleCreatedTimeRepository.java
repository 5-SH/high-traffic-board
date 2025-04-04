package traffic.board.hotarticle.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Repository
@RequiredArgsConstructor
public class ArticleCreatedTimeRepository {
    private final StringRedisTemplate redisTemplate;

    // hot-article::article::{articleId}::created-time
    private static final String KEY_FORMAT = "hot-article::article::{articleId}::created-time";

    public void createOrUpdate(Long articleId, LocalDateTime createdAt, Duration ttl) {
         redisTemplate.opsForValue().set(
                 generateKey(articleId),
                 String.valueOf(createdAt.toInstant(ZoneOffset.UTC).toEpochMilli()),
                 ttl
         );

        /**
         * 좋아요 이벤트가 왔는데, 이 이벤트에 대한 게시글이 오늘 작성된 게시글인지 확인하려면, 게시글 서비스에 대한 조회가 필요함
         * 하지만 게시글 생성 시간을 hot-article 서비스에서 저장하고 있으면, 게시글이 언제 작성 되었는지
         * article 서비스에 요청을 하지 않아도 알 수 있다.
         */
    }

    public void delete(Long articleId) {
        redisTemplate.delete(generateKey(articleId));
    }

    public LocalDateTime read(Long articleId) {
        String result = redisTemplate.opsForValue().get(generateKey(articleId));
        if (result == null) {
            return null;
        }
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(Long.valueOf(result)),
                ZoneOffset.UTC
        );
    }

    private String generateKey(Long articleId) {
        return KEY_FORMAT.formatted(articleId);
    }
}
