package leesh.devcom.backend.config;

import leesh.devcom.backend.common.GlobalProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {GlobalProperties.Server.class})
public class SpringConfig {
}
