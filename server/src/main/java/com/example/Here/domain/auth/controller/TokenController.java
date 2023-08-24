package com.example.Here.domain.auth.controller;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.service.KakaoAuthService;
import com.example.Here.domain.auth.service.RedisService;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    private final KakaoAuthService kakaoAuthService;

    private final RedisService redisService;

    @PostMapping("/token")
        public ResponseEntity<?> createToken(@RequestBody Map<String, String> payload) throws JsonProcessingException {

        String code = payload.get("code");
        log.info("code : {}", code);

        Map<String, String> tokens = kakaoAuthService.getTokensFromKakao(code);

        String kakaoAccessToken = tokens.get("access_token");
        String kakaoRefreshToken = tokens.get("refresh_token");

        Integer expiresIn = Integer.parseInt(tokens.get("expires_in"));
        Integer refreshTokenExpiresIn = Integer.parseInt(tokens.get("refresh_token_expires_in"));

        log.info("kakaoAccessToken : {}", kakaoAccessToken);
        log.info("kakaoRefreshToken : {}", kakaoRefreshToken);
        log.info("expiresIn : {}", expiresIn);
        log.info("refreshTokenExpiresIn : {}", refreshTokenExpiresIn);

        Member authMember = kakaoAuthService.kakaoUserInfo(kakaoAccessToken);

        redisService.saveKakaoToken(authMember.getEmail(), kakaoAccessToken, kakaoRefreshToken, expiresIn, refreshTokenExpiresIn);

        String jwtToken = jwtTokenProvider.generateAccessToken(authMember);
        String refreshJwtToken = jwtTokenProvider.generateRefreshToken(authMember);

        log.info("jwtToken : {}", jwtToken);
        log.info("refreshJwtToken : {}", refreshJwtToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        httpHeaders.add("RefreshToken", refreshJwtToken);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("RefreshToken") String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String newAccessToken = "Bearer " + jwtTokenProvider.createAccessTokenWithRefreshToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("로그인 시간이 만료됐습니다. 다시 로그인해주세요.");
            //클라이언트측과 406으로 처리하기로 합의
        }
    }


}
