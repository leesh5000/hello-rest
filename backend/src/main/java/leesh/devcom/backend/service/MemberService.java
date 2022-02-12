package leesh.devcom.backend.service;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Member member) {
        return memberRepository.save(member).getId();
    }

}
