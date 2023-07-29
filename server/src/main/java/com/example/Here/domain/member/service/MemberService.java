package com.example.Here.domain.member.service;

import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExcepotionCode;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getOrCreateMember(String email, String nickname) {

        // 이메일을 통해 Member가 존재하는지 확인
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            // Member가 존재한다면 그 Member를 반환
            return optionalMember.get();
        } else {
            // Member가 존재하지 않는다면 새 Member를 생성하고 저장
            Member newMember = Member.builder()
                    .email(email)
                    .nickName(nickname)
                    .build();
            return memberRepository.save(newMember);
        }
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExcepotionCode.MEMBER_NOT_FOUND));
    }
}
