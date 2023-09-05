package com.example.Here.domain.member.controller;


import com.example.Here.domain.card.dto.CardPageDto;
import com.example.Here.domain.card.service.CardService;
import com.example.Here.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    private final CardService cardService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        memberService.logout();
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃이 완료되었습니다.");

    }

    @GetMapping("/mypage/createCards")
    public ResponseEntity<CardPageDto> getCreateCard(@RequestParam int page, @RequestParam int size) {

        CardPageDto cardPageDto = cardService.getCreatedCards(page, size);
        return new ResponseEntity<>(cardPageDto, HttpStatus.OK);
    }

    @GetMapping("/mypage/receiveCards")
    public ResponseEntity<CardPageDto> getReceiveCard(@RequestParam int page, @RequestParam int size) {

        CardPageDto cardPageDto = cardService.getReceivedCards(page, size);
        return new ResponseEntity<>(cardPageDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMember() {

        memberService.deleteMember();
        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다.");
    }

}
