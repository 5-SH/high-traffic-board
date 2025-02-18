package traffic.board.articleread.api;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;
import traffic.board.articleread.service.ArticleReadService;
import traffic.board.articleread.service.response.ArticleReadResponse;

public class ArticleReadApiTest {
    RestClient restClient = RestClient.create("http://localhost:9005");

    /**
     * articleId = 150065520896589824
     * articleId = 150065549602406400
     * articleId = 150065555738673152
     * articleId = 150065560327241728
     * articleId = 150065570712338432
     * articleId = 150065579163860992
     * articleId = 150065587489554432
     * articleId = 150065595559395328
     * articleId = 150065601255260160
     * articleId = 150065609987801088
     * articleId = 150065614505066496
     * articleId = 150065623606706176
     * articleId = 150065627499020288
     * articleId = 150065631626215424
     * articleId = 150065645287063552
     * articleId = 150065651012288512
     * articleId = 150065653944107008
     * articleId = 150065660487221248
     * articleId = 150065667344908288
     * articleId = 150065679139291136
     * articleId = 150065691135000576
     * articleId = 150065695924895744
     * articleId = 150065704560967680
     * articleId = 150065714857984000
     * articleId = 150065716451819520
     * articleId = 150065735204552704
     * articleId = 150065737704357888
     * articleId = 150065741223378944
     * articleId = 150065746420121600
     * articleId = 150065748651491328
     */

    @Test
    void readTest() {
        ArticleReadResponse response = restClient.get()
                .uri("/v1/articles/{articleId}", 145696294565564416L)
                .retrieve()
                .body(ArticleReadResponse.class);

        System.out.println("response = " + response);
    }
}
