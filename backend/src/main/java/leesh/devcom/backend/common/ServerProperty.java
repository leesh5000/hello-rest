package leesh.devcom.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "server")
@ConstructorBinding
public class ServerProperty {

    private final String scheme;
    private final String address;
    private final Integer port;

}
