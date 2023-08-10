package com.example.Here.domain.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KakaoTokenService {

    private final KakaoAuthService kakaoAuthService;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisService redisService;

    public KakaoTokenService(KakaoAuthService kakaoAuthService, StringRedisTemplate stringRedisTemplate, RedisService redisService) {
        this.kakaoAuthService = kakaoAuthService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisService = redisService;
    }


    public String verifyAndRefreshKakaoToken(String email) throws JsonProcessingException {
        // 암호화된 토큰을 가져옴
        String encryptedAccessToken = stringRedisTemplate.opsForValue().get(email + ":kakaoAccessToken");
        if (encryptedAccessToken == null) {
            throw new RuntimeException("Access token not found, please re-login.");
        }

        // 복호화
        String kakaoAccessToken = redisService.decryptToken(encryptedAccessToken);

        //유효한 토큰인지 확인
        boolean isValid = kakaoAuthService.validateKakaoAccessToken(kakaoAccessToken);

        if (!isValid) {

            // 토큰이 유효하지 않으면 refresh token을 가져옴
            String encryptedRefreshToken = stringRedisTemplate.opsForValue().get(email + ":kakaoRefreshToken");
            if (encryptedRefreshToken == null) {
                throw new RuntimeException("Refresh token not found, please re-login.");
            }

            //리프레시 토큰 복호화
            String kakaoRefreshToken = redisService.decryptToken(encryptedRefreshToken);

            Map<String, String> refreshedTokens = kakaoAuthService.refreshKakaoTokens(kakaoRefreshToken);

            // 새로운 토큰과 리프레시 토큰을 저장
            redisService.saveKakaoToken(email, refreshedTokens.get("access_token"), refreshedTokens.get("refresh_token"),
                    Integer.parseInt(refreshedTokens.get("expires_in")), Integer.parseInt(refreshedTokens.get("refresh_token_expires_in")));

            // 저장 후 새로운 액세스 토큰 반환
            return refreshedTokens.get("access_token");
        } else {
            // 여전히 유효한 토큰이면 그대로 반환
            return kakaoAccessToken;
        }
    }


}
