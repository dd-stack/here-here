package com.example.Here.domain.card.processor;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardProcessor {

    private final CardRepository cardRepository;

    private final InvitationRepository invitationRepository;

    public Card findCardById(String cardId) {

        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));
    }

    public void checkCardCreator(Member member, Card card) {

        if (!card.getCreator().getEmail().equals(member.getEmail())) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }
    }

    public void softDeleteCard(Card card) {

        card.setDeleted(true);
        cardRepository.save(card);
    }

    public void softDeleteInvitation(Card card){

        invitationRepository.updateDeletedStatusForInvitationsByCard(card, true);
    }


}
