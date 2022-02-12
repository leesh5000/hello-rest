package leesh.devcom.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterResponse {

    private Long id;

    @Builder
    public RegisterResponse(Long id) {
        this.id = id;
    }
}
