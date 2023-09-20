package com.example.Here.domain.card.processor;

import com.example.Here.domain.card.dto.CardDtoToPage;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardProcessor {

    private final CardRepository cardRepository;

    public Card findCardById(String cardId) {

        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));
    }

    public void checkCardCreator(Member member, Card card) {

        if (!card.getCreator().getEmail().equals(member.getEmail())) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.MEMBER_NO_PERMISSION);
        }
    }

    public Page<CardDtoToPage> getCreatedCardsPage(Member member, Pageable pageable) {

        Page<Card> createdCards = cardRepository.findByCreator(member, pageable);
        return createdCards.map(CardDtoToPage::new);
    }

    public Page<CardDtoToPage> getReceivedCardsPage(Member member, Pageable pageable) {

        Page<Card> receivedCards = cardRepository.findCardsByReceiver(member, pageable);
        return receivedCards.map(CardDtoToPage::new);
    }

}
