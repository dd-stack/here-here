package com.example.Here.domain.member.controller;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.service.KakaoService;
import com.example.Here.domain.member.service.MemberService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;

    private final StringRedisTemplate stringRedisTemplate;

    private final KakaoService kakaoService;

    private final MemberService memberService;

    public MemberController(JwtTokenProvider jwtTokenProvider, StringRedisTemplate stringRedisTemplate, KakaoService kakaoService, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.stringRedisTemplate = stringRedisTemplate;
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails)authentication.getPrincipal()).getUsername();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token.");
        }

        memberService.logout(email);

        return ResponseEntity.status(HttpStatus.OK).body("Logout successful.");
    }
}
