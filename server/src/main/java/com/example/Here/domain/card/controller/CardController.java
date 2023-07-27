package com.example.Here.domain.card.controller;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.service.CardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/making")
@Slf4j
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @PostMapping("/card")
    public ResponseEntity<?> createCard(@RequestBody CardDto cardDto) {

        return new ResponseEntity<>(cardService.createCard(cardDto), HttpStatus.OK);
    }

    @GetMapping("/getcard")
    public ResponseEntity<?> getCard(@RequestParam String id) throws JsonProcessingException {
        //카드 생성시에 줬던 카드id를 다시 받아서 카드를 불러옴
        return new ResponseEntity<>(cardService.getCard(id), HttpStatus.OK);
    }

}
