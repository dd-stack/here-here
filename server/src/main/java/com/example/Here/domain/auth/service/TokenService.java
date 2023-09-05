package com.example.Here.domain.auth.service;

import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.processor.TokenProcessor;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import com.example.Here.global.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final TokenProcessor tokenProcessor;

    public String getNewAccessToken(String refreshToken){

        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            return "Bearer " + jwtTokenProvider.createAccessTokenWithRefreshToken(refreshToken);
        }

        else throw new SecurityAuthException(SecurityAuthExceptionCode.REFRESH_TOKEN_NOT_FOUND);
    }

    public HttpHeaders createToken(Map<String, String> payload) throws JsonProcessingException {

        String code = payload.get("code");
        Map<String, String> tokens = tokenProcessor.setTokens(code);
        String kakaoAccessToken = tokens.get("access_token");

        Member authMember = tokenProcessor.getAuthMember(kakaoAccessToken);
        tokenProcessor.saveTokens(tokens, authMember);

        String jwtToken = tokenProcessor.generateAccessToken(authMember);
        String refreshJwtToken = tokenProcessor.generateRefreshToken(authMember);

        return HttpUtils.createHeaderWithRefreshToken(jwtToken, refreshJwtToken);
    }


}
