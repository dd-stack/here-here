package com.example.Here.domain.card.service;

import com.example.Here.domain.auth.service.AuthenticationService;
import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.dto.CardDtoToPage;
import com.example.Here.domain.card.dto.CardPageDto;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.processor.CardProcessor;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    private final InvitationRepository invitationRepository;

    private final CardProcessor cardProcessor;

    private final AuthenticationService authenticationService;

    @Transactional
    public CardDto.Response createCard(CardDto cardDto) {

        Member creator = authenticationService.getAuthenticatedMember();

        Card newCard = new Card(cardDto.getTitle(), cardDto.getStartTime(), cardDto.getEndTime(), cardDto.getBackground(), cardDto.getContent(), cardDto.getTextLocation(), cardDto.getTextColor(), cardDto.getLocation(), creator);
        newCard = cardRepository.save(newCard);

        return new CardDto.Response(newCard.getId());

    }

    public CardDto getCard(String id) throws JsonProcessingException {

        Card card = cardProcessor.findCardById(id);

        // 클라이언트에 넘겨주는 정보에 만든 사람 email도 넣어줌 - 클라이언트에서 만든 사람과 로그인한 사람이 같은지 확인하기 위해
        // 만든 사람이 초대를 수락할 수 없게 클라이언트에서 수락버튼을 안보이게 함
        CardDto cardDto = new CardDto(card.getTitle(), card.getStartTime(), card.getEndTime(), card.getBackground(), card.getContent(), card.getTextLocation(), card.getTextColor(), card.getLocation(), card.getCreator().getEmail());

        String json = ObjectMapperUtil.writeValueAsString(cardDto);
        log.info("json : {}", json);

        return cardDto;

    }

    @Transactional
    public void deleteCard(String id) {

        Member member = authenticationService.getAuthenticatedMember();
        Card deleteCard = cardProcessor.findCardById(id);

        cardProcessor.checkCardCreator(member, deleteCard);

        deleteCard.setDeleted(true);
        cardRepository.save(deleteCard);
        invitationRepository.updateDeletedStatusForInvitationsByCard(deleteCard, true);

    }

    public CardPageDto getCreatedCards(int page, int size) {

        Member member = authenticationService.getAuthenticatedMember();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CardDtoToPage> createdCards = cardProcessor.getCreatedCardsPage(member, pageRequest);

        return new CardPageDto(createdCards);

    }

    public CardPageDto getReceivedCards(int page, int size) {

        Member member = authenticationService.getAuthenticatedMember();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CardDtoToPage> receivedCards = cardProcessor.getReceivedCardsPage(member, pageRequest);

        return new CardPageDto(receivedCards);

    }

}
