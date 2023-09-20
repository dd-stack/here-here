package com.example.Here.domain.auth.processor;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.service.KakaoAuthService;
import com.example.Here.domain.auth.service.RedisService;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TokenProcessor {

    private final KakaoAuthService kakaoAuthService;

    private final RedisService redisService;

    private final JwtTokenProvider jwtTokenProvider;

    public Map<String, String> setTokens(String code) {

        return kakaoAuthService.getTokensFromKakao(code);
    }

    public Member getAuthMember(String accessToken) {

        return kakaoAuthService.kakaoUserInfo(accessToken);
    }

    public void saveTokens(Map<String, String> tokens, Member authMember) {

        String kakaoAccessToken = tokens.get("access_token");
        String kakaoRefreshToken = tokens.get("refresh_token");

        Integer expiresIn = Integer.parseInt(tokens.get("expires_in"));
        Integer refreshTokenExpiresIn = Integer.parseInt(tokens.get("refresh_token_expires_in"));

        redisService.saveKakaoToken(authMember.getEmail(), kakaoAccessToken, kakaoRefreshToken, expiresIn, refreshTokenExpiresIn);
    }

    public String generateAccessToken(Member authMember) {

        return jwtTokenProvider.generateAccessToken(authMember);
    }

    public String generateRefreshToken(Member authMember) {

        return jwtTokenProvider.generateRefreshToken(authMember);
    }

}
