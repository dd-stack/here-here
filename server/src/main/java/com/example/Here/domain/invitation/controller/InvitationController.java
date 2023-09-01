package com.example.Here.domain.invitation.controller;

import com.example.Here.domain.auth.service.AuthenticationService;
import com.example.Here.domain.invitation.Service.InvitationService;
import com.example.Here.domain.member.dto.MemberDtoToAcceptList;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping("/accept/{cardId}")
    public ResponseEntity<Void> acceptInvitation(@PathVariable String cardId) {

        invitationService.acceptInvitation(cardId);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable String cardId) {

        invitationService.deleteInvitation(cardId);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/checkmember/{cardId}")
    public ResponseEntity<List<MemberDtoToAcceptList>> checkInvitationMember(@PathVariable String cardId) {

        return ResponseEntity.ok(invitationService.getAcceptedMembersByCardId(cardId));
    }


}
