package com.example.Here.domain.auth.jwt;

import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.processor.MemberProcessor;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    private final MemberProcessor memberProcessor;

    private final StringRedisTemplate stringRedisTemplate;

    private final JwtTokenManager tokenManager;

    private final JwtTokenValidator tokenValidator;


    public String generateAccessToken(Member member) {

        return tokenManager.buildToken(member, accessTokenExpirationMinutes);
    }

    public String generateRefreshToken(Member member) {

        String refreshToken = tokenManager.buildToken(member, refreshTokenExpirationMinutes);
        tokenManager.saveRefreshToken(member, refreshToken, stringRedisTemplate, refreshTokenExpirationMinutes);
        return refreshToken;
    }

    public boolean validateToken(String token) {

        return tokenValidator.validateToken(token);
    }

    public boolean validateRefreshToken(String refreshToken) {

        return tokenValidator.validateRefreshToken(refreshToken, stringRedisTemplate);
    }

    public Authentication getAuthentication(String token) {

        String email = getEmailFromToken(token);
        Member member = getMemberByEmail(email);

        return new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
    }

    public String createAccessTokenWithRefreshToken(String refreshToken) {

        if (!validateRefreshToken(refreshToken)) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.TOKEN_EXPIRED);
        }

        Claims claims = tokenValidator.parseClaims(refreshToken);
        String email = claims.get("email", String.class);
        Member member = memberProcessor.getMember(email);

        return generateAccessToken(member);
    }

    private String getEmailFromToken(String token) {

        Claims claims = tokenValidator.parseClaims(token);
        String email = claims.get("email", String.class);
        if (email == null || email.isEmpty()) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.EMAIL_NOT_FOUND);
        }
        return email;
    }

    private Member getMemberByEmail(String email) {

        Member member = memberProcessor.getMember(email);
        if (member == null) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.MEMBER_NOT_FOUND);
        }
        return member;
    }

}
