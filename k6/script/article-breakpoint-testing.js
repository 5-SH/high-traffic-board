import http from 'k6/http';
import { check } from 'k6';

export const options = {
    scenarios: {
        breakpoint_test: {
            executor: 'ramping-arrival-rate', //Assure load increase if the system slows
            preAllocatedVUs: 1,
            maxVUs:20000,
            startRate: 0,
            timeUnit: '1s',
            stages: [
                { duration: '2h', target: 20000 }, // just slowly ramp-up to a HUGE load
            ],
        }
    },
    thresholds: {
        'http_req_failed': [{ threshold: 'rate<0.01', abortOnFail: true }], // 오류율이 1% 미만이어야 함
        'http_req_duration': [{ threshold: 'p(95)<500', abortOnFail: true }],  // 99%의 요청이 500ms 미만이어야 함
    }
};

const preprocessJsonString = jsonString => jsonString.replace(/(\d{15,})/g, '"$1"');

const parseHelper = (data, id) => JSON.parse(preprocessJsonString(data), (key, value) => key === id ? value.toString() : value);

const range = (length, start = -1) => Array.from({ length }, (_, i) => start + i + 1);

export default function () {
    const randomPage = Math.floor(Math.random() * 30) + 1;

    const articleListResponse = http.get(http.url`http://localhost:9000/v1/articles?boardId=1&pageSize=10&page=${randomPage}`);
    check(articleListResponse, { 'status was 200': r => r.status === 200 });

    const articleIdList = parseHelper(articleListResponse.body, 'articleId').articles.map(a => a.articleId);

    const randomId = articleIdList[Math.floor(Math.random() * articleIdList.length)];

    const articleResponse = http.get(http.url`http://localhost:9000/v1/articles/${randomId}`);
    check(articleResponse, { 'status was 200': r => r.status === 200 });

    const article = parseHelper(articleResponse.body);
    
    const randomUserId = Math.floor(Math.random() * 1000);
    const viewResponse = http.post(http.url`http://localhost:9003/v1/article-views/articles/${randomId}/users/${randomUserId}`);
    check(viewResponse, { 'status was 200': r => r.status === 200 });
}