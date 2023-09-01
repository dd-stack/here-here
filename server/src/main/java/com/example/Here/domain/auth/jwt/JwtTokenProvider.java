package com.example.Here.domain.auth.jwt;

import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.processor.MemberProcessor;
import com.example.Here.domain.member.service.MemberService;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenProvider {

    private Key key;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    private final MemberProcessor memberProcessor;

    private final StringRedisTemplate stringRedisTemplate;


    public JwtTokenProvider(@Value("${jwt.key}") String secretKey, MemberProcessor memberProcessor, StringRedisTemplate stringRedisTemplate){
        this.memberProcessor = memberProcessor;
        this.stringRedisTemplate = stringRedisTemplate;

        String base64Key = Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);

        this.key = Keys.hmacShaKeyFor(keyBytes);

    }


    public String generateAccessToken(Member member){
        return buildToken(member, accessTokenExpirationMinutes);
    }

    public String generateRefreshToken(Member member){
        String refreshToken = buildToken(member, refreshTokenExpirationMinutes);
        stringRedisTemplate.opsForValue().set(member.getEmail() + ":refresh", refreshToken, refreshTokenExpirationMinutes, TimeUnit.MINUTES);

        return refreshToken;
    }

    private String buildToken(Member member, int expirationMinutes){
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expirationMinutes);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        claims.put("nickname", member.getNickName());
        claims.put("profileImageURL", member.getProfileImageURL());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(expiryTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    //to-do : 예외처리 수정
    public Authentication getAuthentication(String token) {
        try {
            Claims claims = parseClaims(token);
            String email = claims.get("email", String.class);

            if (email == null || email.isEmpty()) {
                throw new IllegalArgumentException("Invalid token: no email claim");
            }

            Member member = memberProcessor.getMember(email);

            if (member == null) {
                throw new UsernameNotFoundException("No user found with email: " + email);
            }

            return new UsernamePasswordAuthenticationToken(member, "", Collections.emptyList());
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Failed to decode token", e);
        }
    }


    public String createAccessTokenWithRefreshToken(String refreshToken) {
        if(!validateRefreshToken(refreshToken)) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.TOKEN_NOT_FOUND);
        }

        Claims claims = parseClaims(refreshToken);
        String email = claims.get("email", String.class);
        Member member = memberProcessor.getMember(email);
        return generateAccessToken(member);
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = parseClaims(refreshToken);
            String email = claims.get("email", String.class);

            // 토큰에 이메일이 없으면 false를 반환
            if (email == null || email.isEmpty()) {
                return false;
            }

            String redisRefreshToken = stringRedisTemplate.opsForValue().get(email + ":refresh");

            // Redis에 저장된 토큰이 없으면 false를 반환
            if (redisRefreshToken == null) {
                return false;
            }

            // 클라이언트가 제출한 토큰이 Redis에 저장된 토큰과 일치하면 true를 반환
            return refreshToken.equals(redisRefreshToken);

        } catch (JwtException | IllegalArgumentException e) {
            // 토큰 파싱 중 오류가 발생하면 false를 반환
            return false;
        }
    }


    public Claims parseClaims(String jws){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws)
                .getBody();
    }
}
