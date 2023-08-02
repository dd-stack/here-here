package com.example.Here.domain.card.service;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.dto.CardDtoListToPage;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.service.MemberService;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Transactional
    public CardDto.Response createCard(CardDto cardDto, Member creator) {

        Card newCard =new Card(cardDto.getTitle(), cardDto.getStartTime(), cardDto.getEndTime(), cardDto.getBackground(), cardDto.getContent(), cardDto.getTextLocation(), cardDto.getTextColor(), cardDto.getLocation(), creator);
        newCard = cardRepository.save(newCard);

        CardDto.Response response = new CardDto.Response(newCard.getId());

        return response;

    }

    public CardDto getCard(String id) throws JsonProcessingException {

        Card card = cardRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));

        // 클라이언트에 넘겨주는 정보에 만든 사람 email도 넣어줌 - 클라이언트에서 만든 사람과 로그인한 사람이 같은지 확인하기 위해
        // 만든 사람이 초대를 수락할 수 없게 클라이언트에서 수락버튼을 안보이게 함
        CardDto cardDto = new CardDto(card.getTitle(), card.getStartTime(), card.getEndTime(), card.getBackground(), card.getContent(), card.getTextLocation(), card.getTextColor(), card.getLocation(), card.getCreator().getEmail());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(cardDto);
        log.info("json : {}", json);

        return cardDto;
    }

    @Transactional
    public void deleteCard(String id, Member member) {

        Card deleteCard = cardRepository.findById(id).orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));
        Member creator = deleteCard.getCreator();
        String creatorEmail = creator.getEmail();
        String memberEmail = member.getEmail();

        if(creatorEmail.equals(memberEmail)) {
            deleteCard.setDeleted(true);
            cardRepository.save(deleteCard);
        }

        else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);
        }

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
