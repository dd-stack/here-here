package com.example.Here.domain.member.service;

import com.example.Here.domain.auth.service.KakaoAuthService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberManageService {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    private final KakaoAuthService kakaoAuthService;

    private final StringRedisTemplate stringRedisTemplate;


    public MemberManageService(MemberService memberService, MemberRepository memberRepository, KakaoAuthService kakaoAuthService, StringRedisTemplate stringRedisTemplate) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.kakaoAuthService = kakaoAuthService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Transactional
    public void logout(String email) {

        // refresh token을 삭제합니다.
        stringRedisTemplate.delete(email + ":refresh");

        // 카카오 토큰을 무효화합니다.
        kakaoAuthService.logoutKakaoToken(email);

        // SecurityContext를 초기화합니다.
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void deleteMember(String email) {
        Member member = memberService.getMember(email);
        member.setDeleted(true);
    }
}
