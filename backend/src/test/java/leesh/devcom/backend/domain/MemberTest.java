package leesh.devcom.backend.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    void isPojo() {

        String email = "test1@gmail.com";
        String username = "test1";
        String password = "1111";
        Member test = Member.createMember(email, username, password);
        Assertions.assertThat(email).isEqualTo(test.getEmail());
        Assertions.assertThat(username).isEqualTo(test.getUsername());
        Assertions.assertThat(password).isEqualTo(test.getPassword());
    }
}