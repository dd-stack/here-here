package com.example.Here.domain.auth.service;

import com.example.Here.domain.auth.KakaoUser;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class KakaoService {

    private final static String clientId = System.getenv("CLIENT_ID");
    private final static String clietnSecret = System.getenv("CLIENT_SECRET");

    private final static String encryptionSecret = System.getenv("ENCRTYPTION_SECRET");

    private final  static String encryptionSalt = System.getenv("ENCRTYPTION_SALT");

    private final MemberService memberService;

    private final StringRedisTemplate stringRedisTemplate;

    private final TextEncryptor textEncryptor;


    public KakaoService(MemberService memberService, StringRedisTemplate stringRedisTemplate, TextEncryptor textEncryptor) {
        this.memberService = memberService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.textEncryptor = textEncryptor;
    }


    public Map<String, String> getTokensFromKakao(String code) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        final String requestUrl = UriComponentsBuilder.fromHttpUrl("https://kauth.kakao.com/oauth/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", "http://localhost:3000/login/oauth2/code/kakao")
                .queryParam("code", code)
                .queryParam("client_secret", clietnSecret)
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl, HttpEntity.EMPTY, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});

    }

    public Member kakaoUserInfo(String kakaoAccessToken) throws JsonProcessingException{

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

            Member member = memberService.getOrCreateMember(kakaoUser.getKakaoAccount().getEmail(), kakaoUser.getKakaoAccount().getProfile().getNickname());

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

    public void saveKakaoToken(String email, String kakaoAccessToken, String kakaoRefreshToken, Integer expiresIn, Integer refreshTokenExpiresIn) {
        stringRedisTemplate.opsForValue().set(email + ":kakaoAccessToken", encryptToken(kakaoAccessToken), expiresIn, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(email + ":kakaoRefreshToken", encryptToken(kakaoRefreshToken), refreshTokenExpiresIn, TimeUnit.SECONDS);
    }

    private String encryptToken(String token) {
        return textEncryptor.encrypt(token);
    }

    private String decryptToken(String encryptedToken) {
        return textEncryptor.decrypt(encryptedToken);
    }

    public String verifyAndRefreshKakaoToken(String email) throws JsonProcessingException {
        // 암호화된 토큰을 가져옴
        String encryptedAccessToken = stringRedisTemplate.opsForValue().get(email + ":kakaoAccessToken");
        if (encryptedAccessToken == null) {
            throw new RuntimeException("Access token not found, please re-login.");
        }

        // 복호화
        String kakaoAccessToken = decryptToken(encryptedAccessToken);

        //유효한 토큰인지 확인
        boolean isValid = validateKakaoAccessToken(kakaoAccessToken);

        if (!isValid) {

            // 토큰이 유효하지 않으면 refresh token을 가져옴
            String encryptedRefreshToken = stringRedisTemplate.opsForValue().get(email + ":kakaoRefreshToken");
            if (encryptedRefreshToken == null) {
                throw new RuntimeException("Refresh token not found, please re-login.");
            }

            //리프레시 토큰 복호화
            String kakaoRefreshToken = decryptToken(encryptedRefreshToken);

            Map<String, String> refreshedTokens = refreshKakaoTokens(kakaoRefreshToken);

            // 새로운 토큰과 리프레시 토큰을 저장
            saveKakaoToken(email, refreshedTokens.get("access_token"), refreshedTokens.get("refresh_token"),
                    Integer.parseInt(refreshedTokens.get("expires_in")), Integer.parseInt(refreshedTokens.get("refresh_token_expires_in")));

            // 저장 후 새로운 액세스 토큰 반환
            return refreshedTokens.get("access_token");
        } else {
            // 여전히 유효한 토큰이면 그대로 반환
            return kakaoAccessToken;
        }
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
                .queryParam("client_secret", clietnSecret)
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl, HttpEntity.EMPTY, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, String>>() {});
    }

    public void logoutKakaoToken(String email) {
        // 이메일로 카카오 액세스 토큰을 가져오는 로직을 구현해야 합니다.
        String kakaoAccessToken = getKakaoTokenByEmail(email);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        restTemplate.exchange("https://kapi.kakao.com/v1/user/logout", HttpMethod.POST, entity, String.class);
    }

    private String getKakaoTokenByEmail(String email) {
        String encryptedRefreshToken = stringRedisTemplate.opsForValue().get(email + ":kakaoAccessToken");
        return decryptToken(encryptedRefreshToken);
    }

    private void removeKakaoToken(String email) {
        stringRedisTemplate.delete(email + ":kakaoAccessToken");
        stringRedisTemplate.delete(email + ":kakaoRefreshToken");
    }




}
