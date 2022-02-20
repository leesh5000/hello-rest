import React from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import {LOGIN_URL} from "../../../util/apiUtil";

function LandingPage() {

    const navigate = useNavigate();

    const onLogoutHandler = () => {
        axios.get('/api/users/logout')
            .then(response => {
                console.log(response.data);
            })
    }

    const onHelloHandler = () => {
        axios.get('/api/hello')
            .then(response => {
                console.log(response.data);
            })
    }

    const onLoginHandler = () => {
        navigate('/login');
    }

    return (
        <div style={{
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            width: '100%', height: '100vh'
        }}>
            <h2>시작 페이지</h2>

            <button onClick={onLogoutHandler}>
                로그아웃
            </button>

            <button onClick={onHelloHandler}>
                Hello
            </button>

            <button onClick={onLoginHandler}>
                Login
            </button>

        </div>
    );
}

export default LandingPage;
