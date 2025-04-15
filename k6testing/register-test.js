import http from 'k6/http';
import { check, sleep } from 'k6';

function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

export let options = {
    vus: 1000,
    duration: '30s',
    iterations: 1000
};

export default function () {
    let username = `user${generateRandomString(8)}`;
    let name = `John Doe ${generateRandomString(4)}`;
    let email = `${generateRandomString(5)}@gmail.com`;

    let payload = JSON.stringify({
        username: username,
        password: 'test',
        name: name,
        email: email
    });

    let headers = {
        'Content-Type': 'application/json',
    };

    let res = http.post('http://localhost:8080/api/v1/auth/register', payload, { headers: headers });

    check(res, {
        'is status 201': (r) => r.status === 200,
    });

    sleep(1);
}
