package leesh.devcom.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Setter
@Getter
public class LoginRequest {

    @Email @NotBlank @Length(max = 100)
    private String email;

    @NotBlank @Length(min = 4, max = 30)
    private String password;

    @Builder
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}