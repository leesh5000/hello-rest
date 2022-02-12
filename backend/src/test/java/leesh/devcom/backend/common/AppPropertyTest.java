package leesh.devcom.backend.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppPropertyTest {

    @Autowired
    AppProperty appProperty;

    @Test
    void bindingTest() {
        String url = appProperty.getUrl();
        String docsUrl = appProperty.getDocsUrl();
        Assertions.assertThat(url).isEqualTo("http://localhost:18080");
        Assertions.assertThat(docsUrl).isEqualTo("http://localhost:18080/docs/index.html");
    }

}