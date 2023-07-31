package com.example.Here.domain.invitation.Service;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final MemberRepository memberRepository;

    private final CardRepository cardRepository;

    public InvitationService(InvitationRepository invitationRepository, MemberRepository memberRepository, CardRepository cardRepository) {
        this.invitationRepository = invitationRepository;
        this.memberRepository = memberRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void acceptInvitation(String cardId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + email));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found: " + cardId));

        Invitation invitation = new Invitation();
        invitation.setReceiver(member);
        invitation.setCard(card);

        invitationRepository.save(invitation);
    }
}
