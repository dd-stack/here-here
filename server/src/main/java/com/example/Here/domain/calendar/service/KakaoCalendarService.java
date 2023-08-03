package com.example.Here.domain.calendar.service;

import com.example.Here.domain.auth.service.KakaoTokenService;
import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.RequestResponseLoggingInterceptor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;


@Service
@Slf4j
public class KakaoCalendarService {

    private static final String URL = "https://kapi.kakao.com/v2/api/calendar/create/event";

    private final KakaoTokenService kakaoTokenService;

    public KakaoCalendarService(KakaoTokenService kakaoTokenService) {
        this.kakaoTokenService = kakaoTokenService;
    }

    public String createEvent(Member member, Event event) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        String email = member.getEmail();
        String accessToken = kakaoTokenService.verifyAndRefreshKakaoToken(email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String eventJson = new ObjectMapper().writeValueAsString(event);
        log.info("eventJson: " + eventJson);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("event", eventJson);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            log.info("Raw response body: " + responseBody);
            Map<String, Object> map = new ObjectMapper().readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            return "톡캘린더 api 호출 성공";
            //return Objects.requireNonNull(map.get("event_id")).toString();
        } else {
            throw new RuntimeException("Failed to create event: " + response.getBody());
        }
    }

}
