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
            memberRepository.save(createMember("admin@gmail.com", "admin", passwordEncoder.encode("1111")));
            log.info("init db done...");
        };
    }
}
