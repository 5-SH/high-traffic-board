import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    iterations: 10,
    thresholds: {
        'http_req_duration': ['p(95)<10'],
        'http_req_failed': ['rate<0.01']
    }
};

export default function () {
    const res = http.get('http://localhost:9000/v1/articles?boardId=1&pageSize=10&page=1');

    check(res, { 'status was 200': r => r.status === 200 });

    sleep(1);
}