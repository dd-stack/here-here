package com.example.Here.domain.card.controller;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
@Slf4j
public class CardController {

    private final CardService cardService;

    @PostMapping("/createcard")
    public ResponseEntity<?> createCard(@RequestBody CardDto cardDto) {

        return new ResponseEntity<>(cardService.createCard(cardDto), HttpStatus.OK);
    }

    @GetMapping("/getcard")
    public ResponseEntity<?> getCard(@RequestParam String cardId) throws JsonProcessingException {
        //카드 생성시에 줬던 카드id를 다시 받아서 카드를 불러옴
        return new ResponseEntity<>(cardService.getCard(cardId), HttpStatus.OK);
    }

    @DeleteMapping("/deletecard")
    public ResponseEntity<?> deleteCard(@RequestParam String cardId) throws JsonProcessingException {

        cardService.deleteCard(cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
