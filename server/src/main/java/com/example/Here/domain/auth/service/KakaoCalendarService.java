package com.example.Here.domain.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
@Slf4j
public class KakaoCalendarService {

    private final RedisService redisService;

    public KakaoCalendarService(RedisService redisService) {
        this.redisService = redisService;
    }

    public String createKakaoCalendarEvent(String accessToken, String calendarId, String eventJson) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("calendar_id", calendarId);
        map.add("event", eventJson);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        String requestUrl = UriComponentsBuilder.fromHttpUrl("https://kapi.kakao.com/v2/api/calendar/create/event")
                .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);

        return responseEntity.getBody();
    }
}
