package com.example.Here.domain.calendar.processor;

import com.example.Here.domain.auth.service.KakaoTokenService;
import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoCalenderProcessor {

    private final KakaoTokenService kakaoTokenService;

    public HttpHeaders createHeader(Member member) throws JsonProcessingException {

        String email = member.getEmail();
        String accessToken = kakaoTokenService.verifyAndRefreshKakaoToken(email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return headers;
    }

    public MultiValueMap<String, String> createBody(Event event) throws JsonProcessingException {

        String eventJson = new ObjectMapper().writeValueAsString(event);
        log.info("eventJson: " + eventJson);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("event", eventJson);

        return body;
    }

    public HttpEntity<MultiValueMap<String, String>> createEntity(Member member, Event event) throws JsonProcessingException {

        return new HttpEntity<>(createBody(event), createHeader(member));
    }

}
