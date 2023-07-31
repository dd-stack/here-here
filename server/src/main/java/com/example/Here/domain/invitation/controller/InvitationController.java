package com.example.Here.domain.invitation.controller;

import com.example.Here.domain.invitation.Service.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invitation")
public class InvitationController {

    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @PostMapping("/card/{cardId}/accept")
    public ResponseEntity<Void> acceptInvitation(@PathVariable String cardId, @RequestBody String email) {
        invitationService.acceptInvitation(cardId, email);
        return ResponseEntity.ok().build();
    }
}
