package com.example.Here.domain.card.dto;

import com.example.Here.domain.card.entity.Card;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class CardPageDto {
    private List<CardDtoListToPage> cards;
    private long totalElements;
    private int currentPage;
    private int size;

    public CardPageDto(Page<CardDtoListToPage> page) {
        this.cards = page.getContent();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
    }
}
