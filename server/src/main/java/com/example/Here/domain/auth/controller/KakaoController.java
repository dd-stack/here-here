package com.example.Here.domain.auth.controller;

import com.example.Here.domain.auth.KakaoUser;
import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class KakaoController {

    private final JwtTokenProvider jwtTokenProvider;

    private final MemberService memberService;

    public KakaoController(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    private final static String clientId = System.getenv("CLIENT_ID");
    private final static String clietnSecret = System.getenv("CLIENT_SECRET");

    @PostMapping("/token")
        public ResponseEntity<?> createToken(@RequestBody Map<String, String> payload) throws JsonProcessingException {

        String code = payload.get("code");

        Map<String, String> tokens = getTokensFromKakao(code);

        String kakaoAccessToken = tokens.get("access_token");
        String kakaoRefreshToken = tokens.get("refresh_token");

        log.info("kakaoAccessToken : {}", kakaoAccessToken);
        log.info("kakaoRefreshToken : {}", kakaoRefreshToken);

        Member authMember = kakaoUserInfo(kakaoAccessToken);

        String jwtToken = jwtTokenProvider.generateAccessToken(authMember);
        String refreshJwtToken = jwtTokenProvider.generateRefreshToken(authMember);

        log.info("jwtToken : {}", jwtToken);
        log.info("refreshJwtToken : {}", refreshJwtToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
        httpHeaders.add("RefreshToken", "Bearer " + refreshJwtToken);

        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    private Map<String, String> getTokensFromKakao(String code) throws JsonProcessingException {

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

    private Member kakaoUserInfo(String kakaoAccessToken) throws JsonProcessingException{

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


}
