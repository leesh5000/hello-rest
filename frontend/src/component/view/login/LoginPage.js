import React, {useState} from 'react';
import {useDispatch} from "react-redux";
import {loginUser} from "../../../_action/user_action";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {LOGIN_URL} from "../../../util/apiUtil";

function LoginPage(props) {

    const dispatch = useDispatch('');
    const navigate = useNavigate();

    /**
     * State
     */
    const [ Email, setEmail ] = useState('')
    const [ Password, setPassword ] = useState('')

    /**
     * Handler
     */
    const onEmailHandler = (event) => {
        setEmail(event.currentTarget.value);
    }

    const OnPasswordHandler = (event) => {
        setPassword(event.currentTarget.value);
    }

    const onSubmitHandler = (event) => {
        event.preventDefault();

        let body = {
            email: Email,
            password: Password
        }

        dispatch(loginUser(body))
            .then(navigate('/'))
            .catch(error => {
                alert('error');
            });
    }

    return (
        <div style={{
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            width: '100%', height: '100vh'}}>
            <form style={{ display: 'flex', flexDirection: 'column'}}
                  onSubmit={onSubmitHandler}>
                <label>Email</label>
                <input type="email" value={Email} onChange={onEmailHandler}/>
                <label>Password</label>
                <input type="password" value={Password} onChange={OnPasswordHandler}/>
                <br/>
                <button>Login</button>
            </form>
        </div>
    );
}

export default LoginPage;
