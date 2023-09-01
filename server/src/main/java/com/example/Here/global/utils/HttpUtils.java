package com.example.Here.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class HttpUtils {

    public static <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, HttpEntity<?> entity, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, method, entity, responseType);
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            throw new RuntimeException("Failed to send HTTP request.");
        }
    }
}
