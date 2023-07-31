package com.example.Here.domain.card.service;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.dto.CardDtoListToPage;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    private final MemberService memberService;

    public CardService(CardRepository cardRepository, MemberService memberService) {
        this.cardRepository = cardRepository;
        this.memberService = memberService;
    }

    public InvitationRepository invitationRepository;


    public CardDto.Response createCard(CardDto cardDto) {

        Member creator = memberService.getMember(cardDto.getEmail());

        Card newCard =new Card(cardDto.getTitle(), cardDto.getStartTime(), cardDto.getEndTime(), cardDto.getBackground(), cardDto.getContent(), cardDto.getTextLocation(), cardDto.getTextColor(), cardDto.getLocation(), creator);
        newCard = cardRepository.save(newCard);

        CardDto.Response response = new CardDto.Response(newCard.getId());

        return response;

    }

    public CardDto getCard(String id) throws JsonProcessingException {

        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 카드가 없습니다. id=" + id));

        CardDto cardDto = new CardDto(card.getTitle(), card.getStartTime(), card.getEndTime(), card.getBackground(), card.getContent(), card.getTextLocation(), card.getTextColor(), card.getLocation());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cardDto);
        log.info("json : {}", json);

        return cardDto;

    }

    public Page<CardDtoListToPage> getCreatedCards(Member member, Pageable pageable) {
        Page<Card> createdCards = cardRepository.findByCreator(member, pageable);

        return createdCards.map(CardDtoListToPage::new);
    }

    public Page<CardDtoListToPage> getReceivedCards(Member member, Pageable pageable) {
       Page<Card> receivedCards = cardRepository.findCardsByReceiver(member, pageable);

       return receivedCards.map(CardDtoListToPage::new);
    }

}
