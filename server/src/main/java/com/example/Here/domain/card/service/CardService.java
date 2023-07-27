package com.example.Here.domain.card.service;

import com.example.Here.domain.card.dto.CardDto;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    public CardDto.Response createCard(CardDto cardDto) {

        Card newCard =new Card(cardDto.getTitle(), cardDto.getStartTime(), cardDto.getEndTime(), cardDto.getBackground(), cardDto.getContent(), cardDto.getTextLocation(), cardDto.getTextColor(), cardDto.getLocation());
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

}
