package com.example.Here.domain.calendar.service;

import com.example.Here.domain.auth.service.KakaoTokenService;
import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;



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

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                log.info("Raw response body: " + responseBody);
                Map<String, Object> map = new ObjectMapper().readValue(responseBody, new TypeReference<Map<String, Object>>() {
                });

                return "톡캘린더 api 호출 성공";
            }

            else {
                log.error("Failed to create event: " + response.getStatusCode());
                throw new BusinessLogicException(ExceptionCode.NOT_VALID_REQUEST);
            }

        } catch (HttpClientErrorException e) {

            // to-do : 예외처리 세분화 필요

            log.error("Failed to create event: " + e.getResponseBodyAsString());
            throw new BusinessLogicException(ExceptionCode.NO_PERMISSION_FOR_CALENDAR);
        }


    }

}
