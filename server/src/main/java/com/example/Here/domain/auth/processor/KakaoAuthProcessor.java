package com.example.Here.domain.auth.processor;

import com.example.Here.domain.auth.service.RedisService;
import com.example.Here.domain.auth.userdto.KakaoUser;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthProcessor {

    private final MemberService memberService;

    private final RedisService redisService;

    public Member getOrCreateMember(KakaoUser kakaoUser) {
        return memberService.getOrCreateMember(kakaoUser.getKakaoAccount().getEmail(), kakaoUser.getKakaoAccount().getProfile().getNickname(), kakaoUser.getKakaoAccount().getProfile().getProfileImageURL());
    }

    public void checkKakaoUser(KakaoUser kakaoUser, ResponseEntity<String> responseEntity) {

        if (kakaoUser == null || kakaoUser.getKakaoAccount() == null) {
            log.error("Failed to get user info from Kakao. The response was: {}", responseEntity.getBody());
            throw new SecurityAuthException(SecurityAuthExceptionCode.KAKAO_USER_NOT_FOUND);
        }

    }

    public String getKakaoToken(String email) {

        String accessToken = redisService.getKakaoTokenByEmail(email);
        if (accessToken == null)
            throw new SecurityAuthException(SecurityAuthExceptionCode.KAKAO_ACCESS_TOKEN_NOT_FOUND);

        return accessToken;
    }

    public String getKakaoRefreshToken(String email) {

        String refreshToken = redisService.getKakaoRefreshTokenByEmail(email);
        if (refreshToken == null)
            throw new SecurityAuthException(SecurityAuthExceptionCode.REFRESH_TOKEN_NOT_FOUND);

        return refreshToken;
    }

    public String saveNewTokens(String email, Map<String, String> tokens) {

        redisService.saveKakaoToken(email, tokens.get("access_token"), tokens.get("refresh_token"),
                Integer.parseInt(tokens.get("expires_in")), Integer.parseInt(tokens.get("refresh_token_expires_in")));

        return tokens.get("access_token");
    }


}
