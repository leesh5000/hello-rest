import React, {useState} from 'react';
import {useDispatch} from "react-redux";
import {useNavigate} from "react-router-dom";
import {loginUser, registerUser} from "../../../_action/user_action";

function RegisterPage(props) {

    const dispatch = useDispatch('');
    const navigate = useNavigate();

    /**
     * State
     */
    const [ Email, setEmail ] = useState('')
    const [ Password, setPassword ] = useState('')
    const [ Name, setName ] = useState('')
    const [ ConfirmPassword, setConfirmPassword ] = useState('')

    /**
     * Handler
     */
    const onEmailHandler = (event) => {
        setEmail(event.currentTarget.value);
    }

    const OnNameHandler = (event) => {
        setName(event.currentTarget.value);
    }

    const OnPasswordHandler = (event) => {
        setPassword(event.currentTarget.value);
    }

    const OnConfirmPasswordHandler = (event) => {
        setConfirmPassword(event.currentTarget.value);
    }

    const onSubmitHandler = (event) => {
        event.preventDefault();

        if (Password !== ConfirmPassword) {
            return alert('비밀번호가 일치하지 않습니다.')
        }

        let body = {
            email: Email,
            name: Name,
            password: Password
        };

        dispatch(registerUser(body))
            .then(response => {
                if (response.payload.success) {
                    navigate('/login');
                } else {
                    alert('Error');
                }
            })
    }

    return (
        <div style={{
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            width: '100%', height: '100vh'}}>
            <form style={{ display: 'flex', flexDirection: 'column'}}
                  onSubmit={onSubmitHandler}>

                <label>Email</label>
                <input type="email" value={Email} onChange={onEmailHandler}/>

                <label>Name</label>
                <input type="text" value={Name} onChange={OnNameHandler}/>

                <label>Password</label>
                <input type="password" value={Password} onChange={OnPasswordHandler}/>

                <label>Confirm Password</label>
                <input type="password" value={ConfirmPassword} onChange={OnConfirmPasswordHandler}/>

                <br/>
                <button>Login</button>
            </form>
        </div>
    );
}

export default RegisterPage;
