import axios from "axios";
import {
    LOGIN_USER, REGISTER_USER
} from './types'

export function loginUser(requestBody) {

    const onSilentRefresh = () => {
        axios.get('/api/users/silent-refresh')
            .then(response => onLoginSuccess(response.data))
            .catch(error => {
                alert('token refresh error');
            })
    }

    const onLoginSuccess = (data) => {
        if (data.success) {
            const tokenType = data.token_type;
            const accessToken = data.access_token;
            const expiryMsec = data.expiry_msec;
            axios.defaults.headers.common['Authorization'] = `${tokenType} ${accessToken}`;

            // refresh access token 1 minute before expiry
            // setTimeout(onSilentRefresh, expiryMsec - 60 * 1000);

            return data;
        } else {
            throw expect();
        }
    }

    const responseBody = axios
        .post('/api/users/login', requestBody)
        .then(response => onLoginSuccess(response.data))
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
