package com.example.Here.domain.calendar.service;

import com.example.Here.domain.auth.service.AuthenticationService;
import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.calendar.processor.KakaoCalenderProcessor;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.example.Here.global.exception.InvalidJsonFormatException;
import com.example.Here.global.utils.HttpUtils;
import com.example.Here.global.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoCalendarService {

    private static final String URL = "https://kapi.kakao.com/v2/api/calendar/create/event";

    private final KakaoCalenderProcessor kakaoCalenderProcessor;

    private final AuthenticationService authenticationService;

    public String createEvent(Event event) {

        Member member = authenticationService.getAuthenticatedMember();

        try {

            ResponseEntity<String> response = HttpUtils.sendRequest(URL, HttpMethod.POST, kakaoCalenderProcessor.createEntity(member, event), String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                log.info("Raw response body: " + responseBody);

                Map<String, Object> map = ObjectMapperUtil.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
                log.info("Parsed response body: " + map);

                return "톡캘린더 api 호출 성공";
            }

            else {
                log.error("Failed to create event: " + response.getStatusCode());
                throw new BusinessLogicException(ExceptionCode.NOT_VALID_REQUEST);
            }

        }

        catch (HttpClientErrorException e) {

            log.error("Failed to create event: " + e.getResponseBodyAsString());
            throw new BusinessLogicException(ExceptionCode.NO_PERMISSION_FOR_CALENDAR);
        }

        catch (JsonProcessingException e) {
            log.error("Failed to create event: " + e.getMessage());
            throw new InvalidJsonFormatException("Invalid JSON format", e);
        }


    }

}
