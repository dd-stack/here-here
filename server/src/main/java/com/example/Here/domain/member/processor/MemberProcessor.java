package com.example.Here.domain.member.processor;

import com.example.Here.domain.auth.service.RedisService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MemberProcessor {

    private final MemberRepository memberRepository;

    private final RedisService redisService;

    //private final KakaoAuthService kakaoAuthService;

    public Member getMember(String email) {

        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public Member updateMemberInfo(Member existingMember, String nickname, String profileImageURL) {

        if (!existingMember.getNickName().equals(nickname)) {
            existingMember.setNickName(nickname);
        }

        if (!Objects.equals(existingMember.getProfileImageURL(), profileImageURL)) {
            existingMember.setProfileImageURL(profileImageURL);
        }

        if (existingMember.isDeleted()) {
            existingMember.setDeleted(false);
        }

        return existingMember;
    }

    public Member createNewMember(String email, String nickname, String profileImageURL) {

        return Member.builder()
                .email(email)
                .nickName(nickname)
                .profileImageURL(profileImageURL)
                .build();
    }

    public void removeTokens(Member member){

        String email = member.getEmail();

        redisService.removeRefreshToken(email);
        redisService.removeKakaoToken(email);
        //kakaoAuthService.logoutKakaoToken(email);

        SecurityContextHolder.clearContext();
    }


}
