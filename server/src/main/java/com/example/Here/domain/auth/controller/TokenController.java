package com.example.Here.domain.auth.controller;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.service.KakaoService;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final JwtTokenProvider jwtTokenProvider;

    private final KakaoService kakaoService;

    public TokenController(JwtTokenProvider jwtTokenProvider, KakaoService kakaoService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoService = kakaoService;
    }

    @PostMapping("/token")
        public ResponseEntity<?> createToken(@RequestBody Map<String, String> payload) throws JsonProcessingException {

        String code = payload.get("code");
        log.info("code : {}", code);

        Map<String, String> tokens = kakaoService.getTokensFromKakao(code);

        String kakaoAccessToken = tokens.get("access_token");
        String kakaoRefreshToken = tokens.get("refresh_token");

        Integer expiresIn = Integer.parseInt(tokens.get("expires_in"));
        Integer refreshTokenExpiresIn = Integer.parseInt(tokens.get("refresh_token_expires_in"));

        log.info("kakaoAccessToken : {}", kakaoAccessToken);
        log.info("kakaoRefreshToken : {}", kakaoRefreshToken);
        log.info("expiresIn : {}", expiresIn);
        log.info("refreshTokenExpiresIn : {}", refreshTokenExpiresIn);

        Member authMember = kakaoService.kakaoUserInfo(kakaoAccessToken);

        kakaoService.saveKakaoToken(authMember.getEmail(), kakaoAccessToken, kakaoRefreshToken, expiresIn, refreshTokenExpiresIn);

        String jwtToken = jwtTokenProvider.generateAccessToken(authMember);
        String refreshJwtToken = jwtTokenProvider.generateRefreshToken(authMember);

        log.info("jwtToken : {}", jwtToken);
        log.info("refreshJwtToken : {}", refreshJwtToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        httpHeaders.add("RefreshToken", "Bearer " + refreshJwtToken);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestHeader("RefreshToken") String refreshToken) {
        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            String newAccessToken = jwtTokenProvider.createAccessTokenWithRefreshToken(refreshToken);
            return ResponseEntity.ok(newAccessToken);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 시간이 만료됐습니다. 다시 로그인해주세요.");
        }
    }



}
