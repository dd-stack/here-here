package com.example.Here.domain.auth.service;

import com.example.Here.domain.auth.processor.KakaoAuthProcessor;
import com.example.Here.domain.auth.userdto.KakaoUser;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.InvalidJsonFormatException;
import com.example.Here.global.utils.HttpUtils;
import com.example.Here.global.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final static String clientId = System.getenv("CLIENT_ID");

    private final static String clientSecret = System.getenv("CLIENT_SECRET");

    private final KakaoAuthProcessor kakaoAuthProcessor;

    public Map<String, String> getTokensFromKakao(String code) {

        try {
            //final String requestUrl = HttpUtils.createRequestUrlToGetToken("https://kauth.kakao.com/oauth/token", "authorization_code", clientId, "http://localhost:3000/login/oauth2/code/kakao", code, clientSecret);
            final String requestUrl = HttpUtils.createRequestUrlToGetToken("https://kauth.kakao.com/oauth/token", "authorization_code", clientId, "https://here-here.co.kr/login/oauth2/code/kakao", code, clientSecret);
            ResponseEntity<String> responseEntity = HttpUtils.sendRequest(requestUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);

            return ObjectMapperUtil.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});
        }

        catch (JsonProcessingException e) {

            log.error("Error occurred while processing JSON: ", e);
            throw new InvalidJsonFormatException("Invalid JSON format", e);
        }


    }


    public boolean validateKakaoAccessToken(String accessToken) {

        try {

            ResponseEntity<String> responseEntity =
                    HttpUtils.sendRequest("https://kapi.kakao.com/v1/user/access_token_info", HttpMethod.GET, HttpUtils.createEntity(accessToken), String.class);

            return responseEntity.getStatusCode() == HttpStatus.OK;

        }

        catch (Exception e) {
            log.error("토큰 값이 잘못되었거나 유효하지 않은 토큰입니다.", e);
            return false;
        }
    }

    public Map<String, String> refreshKakaoTokens(String refreshToken) {

        try {

            final String requestUrl = HttpUtils.createRequestUrlToGetTokenWithRefreshToken("https://kauth.kakao.com/oauth/token", "refresh_token", clientId, refreshToken, clientSecret);
            ResponseEntity<String> responseEntity = HttpUtils.sendRequest(requestUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);
            return ObjectMapperUtil.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});
        }

        catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON: ", e);
            throw new InvalidJsonFormatException("Invalid JSON format", e);
        }

    }

    public String verifyAndRefreshKakaoToken(String email) {

        String kakaoAccessToken = kakaoAuthProcessor.getKakaoToken(email);

        //유효한 토큰인지 확인
        boolean isValid = validateKakaoAccessToken(kakaoAccessToken);

        if (!isValid) {

            String kakaoRefreshToken = kakaoAuthProcessor.getKakaoRefreshToken(email);
            Map<String, String> tokens = refreshKakaoTokens(kakaoRefreshToken);

            return kakaoAuthProcessor.saveNewTokens(email, tokens);
        }

        // 여전히 유효한 토큰이면 그대로 반환
        else return kakaoAccessToken;

    }

    public Member kakaoUserInfo(String kakaoAccessToken) {

        try {

            ResponseEntity<String> responseEntity =
                    HttpUtils.sendRequest("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, HttpUtils.createEntity(kakaoAccessToken), String.class);
            KakaoUser kakaoUser = ObjectMapperUtil.readValue(responseEntity.getBody(), KakaoUser.class);

            kakaoAuthProcessor.checkKakaoUser(kakaoUser, responseEntity);

            return kakaoAuthProcessor.getOrCreateMember(kakaoUser);
        }

        catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON: ", e);
            throw new InvalidJsonFormatException("Invalid JSON format", e);
        }

        catch (Exception e) {
            log.error("Unexpected Error occurred: ", e);
            throw new RuntimeException(e);
        }

    }


/*
    public void logoutKakaoToken(String email) {

        String kakaoAccessToken = redisService.getKakaoTokenByEmail(email);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST, entity, String.class);
    }
*/


}
