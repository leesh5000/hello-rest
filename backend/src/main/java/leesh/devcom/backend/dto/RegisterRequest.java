package leesh.devcom.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterRequest {

    private final String email;
    private final String username;
    private final String password;

    @Builder
    public RegisterRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
