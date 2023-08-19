package com.example.Here.domain.invitation.controller;

import com.example.Here.domain.invitation.Service.InvitationService;
import com.example.Here.domain.member.dto.MemberDtoToAcceptList;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/accept/{cardId}")
    public ResponseEntity<Void> acceptInvitation(@PathVariable String cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();
            invitationService.acceptInvitation(cardId, member);
            return ResponseEntity.ok().build();
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }


    }

    @DeleteMapping("/delete/{cardId}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable String cardId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Member member = (Member) authentication.getPrincipal();
            invitationService.deleteInvitation(cardId, member);
            return ResponseEntity.ok().build();
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

    }

    @GetMapping("/checkmember/{cardId}")
    public ResponseEntity<List<MemberDtoToAcceptList>> checkInvitationMember(@PathVariable String cardId) {

        return ResponseEntity.ok(invitationService.getAcceptedMembersByCardId(cardId));
    }




}
