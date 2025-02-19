package traffic.board.articleread.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.random.RandomGenerator;

public class DataInitializer {
    RestClient articleServiceClient = RestClient.create("http://localhost:9000");
    RestClient commentServiceClient = RestClient.create("http://localhost:9301");
    RestClient likeServiceClient = RestClient.create("http://localhost:9002");
    RestClient viewServiceClient = RestClient.create("http://localhost:9003");

    @Test
    void initialize() {
        for (int i = 0; i < 30; i++) {
            Long articleId = createArticle();
            System.out.println("articleId = " + articleId);
            long commentCount = RandomGenerator.getDefault().nextLong(10);
            long likeCount = RandomGenerator.getDefault().nextLong(10);
            long viewCount = RandomGenerator.getDefault().nextLong(200);

            createComment(articleId, commentCount);
            like(articleId, likeCount);
            view(articleId, viewCount);

        }
    }

    /**
     * articleId = 150414349784850432
     * articleId = 150414521352855552
     * articleId = 150414530903285760
     * articleId = 150414540487270400
     * articleId = 150414551186939904
     * articleId = 150414562591252480
     * articleId = 150414566148022272
     * articleId = 150414583868956672
     * articleId = 150414599895392256
     * articleId = 150414619205967872
     * articleId = 150414659148324864
     * articleId = 150414684024741888
     * articleId = 150414695869456384
     * articleId = 150414709811322880
     * articleId = 150414747107074048
     * articleId = 150414796469837824
     * articleId = 150414815407120384
     * articleId = 150414834973548544
     * articleId = 150414843194384384
     * articleId = 150414849343234048
     * articleId = 150414861913563136
     * articleId = 150414870193119232
     * articleId = 150414874869768192
     * articleId = 150414885011595264
     * articleId = 150414898261401600
     * articleId = 150414925922836480
     * articleId = 150414945199857664
     * articleId = 150414952120459264
     * articleId = 150414970210492416
     * articleId = 150414972827738112
     */

    void createComment(Long articleId, long commentCount) {
        while (commentCount-- > 0) {
            commentServiceClient.post()
                    .uri("/v2/comments")
                    .body(new CommentCreateRequest(articleId, "content", 1L))
                    .retrieve();
        }
    }

    void like(Long articleId, long likeCount) {
        while (likeCount-- > 0) {
            likeServiceClient.post()
                    .uri("/v1/article-likes/articles/{articleId}/users/{userId}/pessimistic-lock-1", articleId, likeCount)
                    .retrieve();
        }
    }

    void view(Long articleId, long viewCount) {
        while (viewCount-- > 0) {
            viewServiceClient.post()
                    .uri("/v1/article-views/articles/{articleId}/users/{userId}", articleId, viewCount)
                    .retrieve();
        }
    }

    private Long createArticle() {
        return articleServiceClient.post()
                .uri("/v1/articles")
                .body(new ArticleCreateRequest("title", "content", 1L, 1L))
                .retrieve()
                .body(ArticleResponse.class)
                .getArticleId();
    }

    @Getter
    @AllArgsConstructor
    static class ArticleCreateRequest {
        private String title;
        private String content;
        private Long writerId;
        private Long boardId;
    }

    @Getter
    @AllArgsConstructor
    static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private Long writerId;
    }

    @Getter
    static class ArticleResponse {
        private Long articleId;

    }
}
