package leesh.devcom.backend.security;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.domain.MemberRepository;
import leesh.devcom.backend.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.singletonList;
import static leesh.devcom.backend.exception.ErrorCode.NOT_EXIST_MEMBER;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(NOT_EXIST_MEMBER));

        List<SimpleGrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .authorities(authorities)
                .build();
    }
}
