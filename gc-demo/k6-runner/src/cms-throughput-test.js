import http from 'k6/http';
import { sleep } from 'k6';
import { randomIntBetween } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export const options = {
    vus: 100,  // 가상 사용자 수
    duration: '30s',  // 테스트 지속 시간
};

const BASE_HOST = 'http://localhost:8081';

export default function () {
    // 랜덤하게 엔드포인트 선택
    const endpoint = ['/run-throughput'];

    // 랜덤한 페이로드 생성
    const payload = JSON.stringify({
        data: `random_${randomIntBetween(1, 1000)}`,
    });

    // HTTP 요청 보내기
    const res = http.get(
        `${BASE_HOST}${endpoint}?size=${randomIntBetween(30, 80)}`,
        payload,
        {headers: { 'Content-Type': 'application/json' }}
    );

    // 랜덤한 시간 동안 대기 (0.1초에서 1초 사이)
    sleep(randomIntBetween(1, 10) / 10);
}
