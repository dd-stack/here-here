package com.example.Here.domain.auth.controller;

import com.example.Here.domain.auth.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<?> createToken(@RequestBody Map<String, String> payload) throws JsonProcessingException {

        return new ResponseEntity<>(tokenService.createToken(payload), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> getNewAccessToken(@RequestHeader("RefreshToken") String refreshToken) {

        return ResponseEntity.ok(tokenService.getNewAccessToken(refreshToken));
    }

}
