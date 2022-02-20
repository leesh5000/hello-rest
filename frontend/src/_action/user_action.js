import axios from "axios";
import {LOGIN_USER, REGISTER_USER} from './types'
import {LOGIN_URL, setAccessToken} from "../util/apiUtil";

export function loginUser(requestBody) {

    const onSilentRefresh = () => {
        axios.get('/api/users/silent-refresh')
            .then(response => onLoginSuccess(response.data))
            .catch(error => {
                alert('token refresh error');
            })
    }

    const onLoginSuccess = (response) => {
        let accessToken = response.data.access_token;
        let expirySec = response.data.expiry_sec;
        setAccessToken(accessToken);

        // refresh access token 1 minute before expiry
        setTimeout(onSilentRefresh, expirySec - 60 * 1000);

        return null;
    }

    const responseBody = axios
        .post(LOGIN_URL, requestBody)
        .then(response => onLoginSuccess(response))
        .catch(error => {
            return false;
        });

    return {
        type: LOGIN_USER,
        payload: responseBody
    }
}

export function registerUser(requestBody) {

    const responseBody = axios
        .post('/api/users/register', requestBody)
        .then(response => response.data);

    return {
        type: REGISTER_USER,
        payload: responseBody
    }
}