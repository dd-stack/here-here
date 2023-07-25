package com.example.Here.domain.auth.repository;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    public void save(String email, String refreshToken){

        long duration = Duration.between(Instant.now(), jwtTokenProvider.parseClaims(refreshToken).getExpiration().toInstant()).getSeconds();
        redisTemplate.opsForValue().set(email, refreshToken, duration, TimeUnit.SECONDS);
    }

    public String findBy(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteBy(String key) {
        redisTemplate.delete(key);
    }

    public void setBlackList(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        Date expiration = claims.getExpiration();

        long duration = Duration.between(Instant.now(), expiration.toInstant()).getSeconds();
        redisTemplate.opsForValue().set(accessToken, "logout", duration, TimeUnit.SECONDS);
    }





}
