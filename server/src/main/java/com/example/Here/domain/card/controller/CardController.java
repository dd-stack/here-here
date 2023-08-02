package com.example.Here.domain.card.controller;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.service.CardService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        Authentication autentication = SecurityContextHolder.getContext().getAuthentication();

        if (autentication != null && autentication.isAuthenticated()) {
            Member member = (Member) autentication.getPrincipal();
            return new ResponseEntity<>(cardService.createCard(cardDto, member), HttpStatus.OK);
        }

        else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

    }

    @GetMapping("/getcard")
    public ResponseEntity<?> getCard(@RequestParam String cardId) throws JsonProcessingException {
        //카드 생성시에 줬던 카드id를 다시 받아서 카드를 불러옴
        return new ResponseEntity<>(cardService.getCard(cardId), HttpStatus.OK);
    }

    @DeleteMapping("/deletecard")
    public ResponseEntity<?> deleteCard(@RequestParam String cardId) throws JsonProcessingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()){
            Member member = (Member) authentication.getPrincipal();
            cardService.deleteCard(cardId, member);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

    }


}
