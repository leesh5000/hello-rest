package leesh.devcom.backend.config;

import leesh.devcom.backend.common.AppProperty;
import leesh.devcom.backend.common.ServerProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {
        AppProperty.class, ServerProperty.class
})
public class SpringConfig {
}
