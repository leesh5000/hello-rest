package leesh.devcom.backend.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServerPropertyTest {

    @Autowired
    ServerProperty serverProperty;

    @Test
    void bindingTest() {
        String scheme = serverProperty.getScheme();
        String address = serverProperty.getAddress();
        Integer port = serverProperty.getPort();
        Assertions.assertThat(scheme).isEqualTo("http");
        Assertions.assertThat(address).isEqualTo("localhost");
        Assertions.assertThat(port).isEqualTo(18080);
    }

}