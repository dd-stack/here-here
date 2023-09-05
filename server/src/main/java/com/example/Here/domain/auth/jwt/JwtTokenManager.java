package com.example.Here.domain.auth.jwt;

import com.example.Here.domain.member.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenManager {

    private Key key;

    public JwtTokenManager(@Value("${jwt.key}")String secretKey) {
        String base64Key = Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String buildToken(Member member, int expirationMinutes) {

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

    public void saveRefreshToken(Member member, String refreshToken, StringRedisTemplate stringRedisTemplate, int refreshTokenExpirationMinutes) {
        stringRedisTemplate.opsForValue().set(member.getEmail() + ":refresh", refreshToken, refreshTokenExpirationMinutes, TimeUnit.MINUTES);
    }
}

