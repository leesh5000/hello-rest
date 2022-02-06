package leesh.devcom.backend.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GlobalPropertiesTest {

    @Autowired
    GlobalProperties globalProperties;

    @Test
    public void serverPropertyTest() {
        assertThat(globalProperties.getServer().getAddress()).isEqualTo("localhost");
        assertThat(globalProperties.getServer().getPort()).isEqualTo(18080);
    }
}