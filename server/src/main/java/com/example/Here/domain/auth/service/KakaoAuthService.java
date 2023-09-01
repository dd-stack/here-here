package com.example.Here.domain.auth.service;

import com.example.Here.domain.auth.userdto.KakaoUser;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
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

    private final RedisService redisService;

    public Map<String, String> getTokensFromKakao(String code) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        final String requestUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                //로컬 테스트용 URI
                .queryParam("redirect_uri", "http://localhost:3000/login/oauth2/code/kakao")
                //.queryParam("redirect_uri", "https://here-here.co.kr/login/oauth2/code/kakao")
                .queryParam("code", code)
                .queryParam("client_secret", clientSecret)
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl, HttpEntity.EMPTY, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});

    }


    public boolean validateKakaoAccessToken(String accessToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange("https://kapi.kakao.com/v1/user/access_token_info", HttpMethod.GET, entity, String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            return false;
        }
    }

    public Map<String, String> refreshKakaoTokens(String refreshToken) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        final String requestUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "refresh_token")
                .queryParam("client_id", clientId)
                .queryParam("refresh_token", refreshToken)
                .queryParam("client_secret", clientSecret)
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl, HttpEntity.EMPTY, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});
    }

    public Member kakaoUserInfo(String kakaoAccessToken) {

        try {

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + kakaoAccessToken);

            HttpEntity httpEntity = new HttpEntity(httpHeaders);

            ResponseEntity<String> responseEntity = restTemplate.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.GET, httpEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            KakaoUser kakaoUser = objectMapper.readValue(responseEntity.getBody(), KakaoUser.class);

            if (kakaoUser == null || kakaoUser.getKakaoAccount() == null) {
                log.error("Failed to get user info from Kakao. The response was: {}", responseEntity.getBody());
                throw new RuntimeException("Failed to get user info from Kakao.");
            }

            Member member = memberService.getOrCreateMember(kakaoUser.getKakaoAccount().getEmail(), kakaoUser.getKakaoAccount().getProfile().getNickname(), kakaoUser.getKakaoAccount().getProfile().getProfileImageURL());

            return member;
        }

        catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON: ", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            throw new RuntimeException(e);
        }

    }

    public void logoutKakaoToken(String email) {

        String kakaoAccessToken = redisService.getKakaoTokenByEmail(email);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST, entity, String.class);
    }




}
