package leesh.devcom.backend.service;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.exception.CustomException;
import leesh.devcom.backend.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void save_fail_test() {
        String email = "leesh@gmail.com";
        String username = "leesh";
        String password = "1111";
        Member member = Member.createMember(email, username, password);
        Long save = memberService.save(member);

        Member newMember = Member.createMember("leesh@gmail.com", "leesh1", "1111");
        CustomException e = assertThrows(CustomException.class, () -> {
            memberService.save(newMember);
        });

        Assertions.assertThat(e.getErrorCode()).isEqualTo(ErrorCode.ALREADY_EXIST_MEMBER);
    }
}