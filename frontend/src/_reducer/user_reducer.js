import {
    LOGIN_USER, REGISTER_USER
} from "../_action/types";

// state = previous state
export default function (state = {}, action) {
    switch (action.type) {
        case LOGIN_USER:
            return {...state, login: action.payload}
        case REGISTER_USER:
            return {...state, register: action.payload}
        default:
            return state;
    }
};
