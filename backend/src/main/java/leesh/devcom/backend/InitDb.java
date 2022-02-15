package leesh.devcom.backend;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.domain.MemberRepository;
import leesh.devcom.backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static leesh.devcom.backend.domain.Member.*;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class InitDb {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadInitData() {
        return args -> {
            log.info("init db start...");
//            memberRepository.save(createMember("test1@gmail.com", "test1", passwordEncoder.encode("1111")));
//            memberRepository.save(createMember("test2@gmail.com", "test2", passwordEncoder.encode("1111")));
//            memberRepository.save(createMember("test3@gmail.com", "test3", passwordEncoder.encode("1111")));
//            memberRepository.save(createMember("test4@gmail.com", "test4", passwordEncoder.encode("1111")));
//            memberRepository.save(createMember("test5@gmail.com", "test5", passwordEncoder.encode("1111")));
//            memberRepository.save(createMember("test6@gmail.com", "test6", passwordEncoder.encode("1111")));
            log.info("init db done...");
        };
    }
}
