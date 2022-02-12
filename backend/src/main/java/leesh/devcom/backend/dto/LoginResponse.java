package leesh.devcom.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class LoginResponse  {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expiry_sec")
    private Long expirySec;

    @Builder
    public LoginResponse(String accessToken, Long expirySec) {
        this.accessToken = accessToken;
        this.expirySec = expirySec;
    }
}
