package com.example.Here.domain.calendar.controller;

import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.calendar.service.KakaoCalendarService;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
@Slf4j
public class CalendarController {

    private final KakaoCalendarService kakaoCalendarService;

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {

        log.info("request : {}", event);

        return ResponseEntity.ok(kakaoCalendarService.createEvent(event));

    }

}
