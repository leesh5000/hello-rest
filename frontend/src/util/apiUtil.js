import axios from "axios";

export const BASE_URL = 'http://localhost:18080';
export const LOGIN_URL = BASE_URL + '/api/auth/login';

export function isOk(response) {
    if (response.status >= 200 && response.status < 400) {
        return true;
    } else {
        return false;
    }
}

export function setAccessToken(accessToken) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
}