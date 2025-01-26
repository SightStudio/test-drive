import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    iterations: 10,
};


export default function () {

    // Make a GET request to the target URL
    http.get('https://test-api.k6.io');

    // Sleep for 1 second to simulate real-world usage
    sleep(1);
}
