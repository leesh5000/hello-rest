import axios from "axios";

export const BASE_URL = 'http://localhost:18080';
export const LOGIN_URL = BASE_URL + '/api/auth/login';

export function setLoginUrl() {

}

export function isOk(response) {
    return response.status >= 200 && response.status < 400;
}

export function setAccessToken(accessToken) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
}