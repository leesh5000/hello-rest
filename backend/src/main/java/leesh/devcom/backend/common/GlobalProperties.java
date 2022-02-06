package leesh.devcom.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;


@Getter
@RequiredArgsConstructor
@Component
public class GlobalProperties {

    private final Server server;

    @Getter
    @RequiredArgsConstructor
    @ConfigurationProperties(prefix = "server")
    @ConstructorBinding
    public static final class Server {
        private final String address;
        private final Integer port;
    }
}
