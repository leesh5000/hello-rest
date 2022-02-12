package leesh.devcom.backend.service;

import leesh.devcom.backend.domain.Member;
import leesh.devcom.backend.domain.MemberRepository;
import leesh.devcom.backend.exception.CustomException;
import leesh.devcom.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long save(@NotNull Member member) {

        memberRepository.findByEmail(member.getEmail())
                .ifPresent(e -> {
                    throw new CustomException(ErrorCode.ALREADY_EXIST_MEMBER);
                });

        return memberRepository.save(member).getId();
    }

}
