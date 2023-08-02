package com.example.Here.domain.calendar.controller;

import com.example.Here.domain.calendar.dto.Event;
import com.example.Here.domain.calendar.service.KakaoCalendarService;
import com.example.Here.domain.member.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
@Slf4j
public class CalendarController {

    private final KakaoCalendarService kakaoCalendarService;

    public CalendarController(KakaoCalendarService kakaoCalendarService) {
        this.kakaoCalendarService = kakaoCalendarService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody Event event) throws JsonProcessingException {

        log.info("request : {}", event);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        String response = kakaoCalendarService.createEvent(member, event);

        return ResponseEntity.ok(response);

    }


}
