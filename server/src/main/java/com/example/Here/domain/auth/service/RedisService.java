package com.example.Here.domain.auth.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    private final TextEncryptor textEncryptor;

    public RedisService(StringRedisTemplate stringRedisTemplate, TextEncryptor textEncryptor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.textEncryptor = textEncryptor;
    }

    public void saveKakaoToken(String email, String kakaoAccessToken, String kakaoRefreshToken, Integer expiresIn, Integer refreshTokenExpiresIn) {
        stringRedisTemplate.opsForValue().set(email + ":kakaoAccessToken", encryptToken(kakaoAccessToken), expiresIn, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(email + ":kakaoRefreshToken", encryptToken(kakaoRefreshToken), refreshTokenExpiresIn, TimeUnit.SECONDS);
    }

    public String encryptToken(String token) {
        return textEncryptor.encrypt(token);
    }

    public String decryptToken(String encryptedToken) {
        return textEncryptor.decrypt(encryptedToken);
    }

    public String getKakaoTokenByEmail(String email) {
        String encryptedRefreshToken = stringRedisTemplate.opsForValue().get(email + ":kakaoAccessToken");
        return decryptToken(encryptedRefreshToken);
    }

    public void removeKakaoToken(String email) {
        stringRedisTemplate.delete(email + ":kakaoAccessToken");
        stringRedisTemplate.delete(email + ":kakaoRefreshToken");
    }

    public void removeRefreshToken(String email) {
        stringRedisTemplate.delete(email + ":refresh");
    }

}
