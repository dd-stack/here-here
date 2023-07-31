package com.example.Here.domain.member.controller;

import com.example.Here.domain.card.dto.CardDtoListToPage;
import com.example.Here.domain.card.dto.CardPageDto;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.service.CardService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberManageService;
import com.example.Here.domain.member.service.MemberService;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExcepotionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberManageService memberManageService;

    private final CardService cardService;

    public MemberController(MemberManageService memberManageService, CardService cardService) {
        this.memberManageService = memberManageService;
        this.cardService = cardService;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token.");
        }

        memberManageService.logout(email);

        return ResponseEntity.status(HttpStatus.OK).body("Logout successful.");
    }

    @GetMapping("/mypage/createcard")
    public CardPageDto getCreatedCard(@RequestParam int page, @RequestParam int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();

            Page<CardDtoListToPage> createdCards = cardService.getCreatedCards(member, PageRequest.of(page, size));

            log.info("createdCards : {}", createdCards.toString());

            return new CardPageDto(createdCards);

        } else {
            throw new BusinessLogicException(ExcepotionCode.MEMBER_NO_PERMISSION);
        }
    }

    @GetMapping("/mypage/receivedcard")
    public CardPageDto getReceivedCard(@RequestParam int page, @RequestParam int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();

            Page<CardDtoListToPage> receivedCards = cardService.getReceivedCards(member, PageRequest.of(page, size));
            return new CardPageDto(receivedCards);


        }

        else {
            throw new BusinessLogicException(ExcepotionCode.MEMBER_NO_PERMISSION);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token.");
        }

        memberManageService.deleteMember(email);

        return ResponseEntity.status(HttpStatus.OK).body("Delete successful.");
    }
}
