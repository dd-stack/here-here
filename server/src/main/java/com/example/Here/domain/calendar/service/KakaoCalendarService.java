package com.example.Here.domain.calendar.service;

import com.example.Here.domain.auth.service.KakaoTokenService;
import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.calendar.processor.KakaoCalenderProcessor;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;



@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoCalendarService {

    private static final String URL = "https://kapi.kakao.com/v2/api/calendar/create/event";

    private final KakaoCalenderProcessor kakaoCalenderProcessor;

    public String createEvent(Event event) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);

        Member member = (Member) authentication.getPrincipal();

        try {
            ResponseEntity<String> response = kakaoCalenderProcessor.postForEntity(URL, member, event);

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
