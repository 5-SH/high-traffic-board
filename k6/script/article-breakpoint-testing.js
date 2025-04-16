import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    executor: 'ramping-arrival-rate', //Assure load increase if the system slows
    stages: [
        { duration: '2h', target: 20000 }, // just slowly ramp-up to a HUGE load
    ],
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
    // console.log('randomPage:', randomPage);

    const articleListResponse = http.get(`http://localhost:9000/v1/articles?boardId=1&pageSize=10&page=${randomPage}`);
    check(articleListResponse, { 'status was 200': r => r.status === 200 });

    const articleIdList = parseHelper(articleListResponse.body, 'articleId').articles.map(a => a.articleId);
    // console.log('articleIdList:', articleIdList);

    const randomId = articleIdList[Math.floor(Math.random() * articleIdList.length)];
    // console.log('randomId:', randomId);

    const articleResponse = http.get(`http://localhost:9000/v1/articles/${randomId}`);
    check(articleResponse, { 'status was 200': r => r.status === 200 });

    const article = parseHelper(articleResponse.body);
    // console.log('article:', article);
}