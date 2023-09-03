package com.example.Here.domain.auth.service;

import com.example.Here.domain.auth.userdto.KakaoUser;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.example.Here.global.utils.HttpUtils;
import com.example.Here.global.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAuthService {

    private final static String clientId = System.getenv("CLIENT_ID");

    private final static String clientSecret = System.getenv("CLIENT_SECRET");

    private final MemberService memberService;

    public Map<String, String> getTokensFromKakao(String code) throws JsonProcessingException {

        final String requestUrl = HttpUtils.createRequestUrlToGetToken("https://kauth.kakao.com/oauth/token", "authorization_code", clientId, "http://localhost:3000/login/oauth2/code/kakao", code, clientSecret);
        //final String requestUrl = HttpUtils.createRequestUrlToGetToken("https://kauth.kakao.com/oauth/token", "authorization_code", clientId, "https://here-here.co.kr/login/oauth2/code/kakao", code, clientSecret);
        ResponseEntity<String> responseEntity = HttpUtils.sendRequest(requestUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);

        return ObjectMapperUtil.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {
        });

    }


    public boolean validateKakaoAccessToken(String accessToken) {

        try {

            HttpEntity<?> entity = new HttpEntity<>(HttpUtils.createHeader(accessToken));
            ResponseEntity<String> responseEntity = HttpUtils.sendRequest("https://kapi.kakao.com/v1/user/access_token_info", HttpMethod.GET, entity, String.class);

            return responseEntity.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            log.error("Error occurred: ", e);
            return false;
        }
    }

    public Map<String, String> refreshKakaoTokens(String refreshToken) throws JsonProcessingException {

        final String requestUrl = HttpUtils.createRequestUrlToGetTokenWithRefreshToken("https://kauth.kakao.com/oauth/token", "refresh_token", clientId, refreshToken, clientSecret);
        ResponseEntity<String> responseEntity = HttpUtils.sendRequest(requestUrl, HttpMethod.POST, HttpEntity.EMPTY, String.class);

        return ObjectMapperUtil.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {
        });

    }

    public Member kakaoUserInfo(String kakaoAccessToken) {

        try {

            HttpEntity<?> entity = new HttpEntity<>(HttpUtils.createHeader(kakaoAccessToken));
            ResponseEntity<String> responseEntity = HttpUtils.sendRequest("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, entity, String.class);
            KakaoUser kakaoUser = ObjectMapperUtil.readValue(responseEntity.getBody(), KakaoUser.class);

            if (kakaoUser == null || kakaoUser.getKakaoAccount() == null) {
                log.error("Failed to get user info from Kakao. The response was: {}", responseEntity.getBody());
                throw new RuntimeException("Failed to get user info from Kakao.");
            }

            return getOrCreateMember(kakaoUser);

        } catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON: ", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            throw new RuntimeException(e);
        }

    }

    private Member getOrCreateMember(KakaoUser kakaoUser) {
        return memberService.getOrCreateMember(kakaoUser.getKakaoAccount().getEmail(), kakaoUser.getKakaoAccount().getProfile().getNickname(), kakaoUser.getKakaoAccount().getProfile().getProfileImageURL());
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
