package com.example.Here.domain.member.controller;

import com.example.Here.domain.card.dto.CardDtoToPage;
import com.example.Here.domain.card.dto.CardPageDto;
import com.example.Here.domain.card.service.CardService;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberManageService;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberManageService memberManageService;

    private final CardService cardService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();

            String email = member.getEmail();

            memberManageService.logout(email);
            return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 완료되었습니다.");

        }

        else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

    }

    @GetMapping("/mypage/createcard")
    public CardPageDto getCreatedCard(@RequestParam int page, @RequestParam int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<CardDtoToPage> createdCards = cardService.getCreatedCards(member, pageRequest);

            return new CardPageDto(createdCards);

        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }
    }

    @GetMapping("/mypage/receivedcard")
    public CardPageDto getReceivedCard(@RequestParam int page, @RequestParam int size) {

        log.info("Received request for page: {}, size: {}", page, size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();

            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<CardDtoToPage> receivedCards = cardService.getReceivedCards(member, pageRequest);

            CardPageDto result = new CardPageDto(receivedCards);

            log.info("Result: {}", result);

            return result;


        }

        else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();
            memberManageService.deleteMember(member);
            return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다.");

        }

        else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

    }
}
