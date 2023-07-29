package com.example.Here.domain.member.service;

import com.example.Here.domain.auth.service.KakaoService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExcepotionCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final StringRedisTemplate stringRedisTemplate;

    private final KakaoService kakaoService;

    public MemberService(MemberRepository memberRepository, StringRedisTemplate stringRedisTemplate, StringRedisTemplate stringRedisTemplate1, KakaoService kakaoService) {
        this.memberRepository = memberRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        this.kakaoService = kakaoService;
    }

    public Member getOrCreateMember(String email, String nickname) {

        // 이메일을 통해 Member가 존재하는지 확인
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {

            // Member가 존재한다면 닉네임이 업데이트 되었는지 확인
            Member existingMember = optionalMember.get();

            if (!existingMember.getNickName().equals(nickname)) {
                // 닉네임이 업데이트 되었다면 해당 정보를 변경하고 저장
                existingMember.setNickName(nickname);
                return memberRepository.save(existingMember);
            }

            return existingMember;

        }

        else {
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

    public void logout(String email) {

        // refresh token을 삭제합니다.
        stringRedisTemplate.delete(email + ":refresh");

        // 카카오 토큰을 무효화합니다.
        kakaoService.logoutKakaoToken(email);

        // SecurityContext를 초기화합니다.
        SecurityContextHolder.clearContext();
    }
}
