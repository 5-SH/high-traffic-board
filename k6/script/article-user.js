import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    iterations: 1,
    vus: 1,
    thresholds: {
        'http_req_duration': ['p(95)<100'],
        'http_req_failed': ['rate<0.01']
    }
};

const preprocessJsonString = jsonString => jsonString.replace(/(\d{15,})/g, '"$1"');

const parseHelper = (data, id) => JSON.parse(preprocessJsonString(data), (key, value) => key === id ? value.toString() : value);

const range = (length, start = -1) => Array.from({ length }, (_, i) => start + i + 1);

export default function () {
    const randomPage = Math.floor(Math.random() * 30);
    console.log('randomPage:', randomPage);

    range(parseInt(randomPage), 1).forEach(page => {
        const articleListResponse = http.get(`http://localhost:9000/v1/articles?boardId=1&pageSize=10&page=${page}`);
        check(articleListResponse, { 'status was 200': r => r.status === 200 });

        const articleIdList = parseHelper(articleListResponse.body, 'articleId').articles.map(a => a.articleId);
        console.log('articleIdList:', articleIdList);

        articleIdList.forEach(id => {
            const articleResponse = http.get(`http://localhost:9000/v1/articles/${id}`);
            check(articleResponse, { 'status was 200': r => r.status === 200 });

            const article = parseHelper(articleResponse.body);
            console.log('article:', article);

            const randomUserId = Math.floor(Math.random() * 1000);
            const viewResponse = http.post(`http://localhost:9003/v1/article-views/articles/${id}/users/${randomUserId}`);
            check(viewResponse, { 'status was 200': r => r.status === 200 });

            sleep(1);
        });
    });
}