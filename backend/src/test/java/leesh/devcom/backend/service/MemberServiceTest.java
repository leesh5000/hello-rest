package leesh.devcom.backend.service;

import leesh.devcom.backend.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void saveTest() {

        String email = "leesh@gmail.com";
        String username = "leesh";
        String password = "1111";
        Member member = Member.createMember(email, username, password);
        Long save = memberService.save(member);

        Assertions.assertThat(save).isGreaterThan(0L);
    }

}