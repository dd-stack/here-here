package com.example.Here.domain.card.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
public class CardPageDto {
    private List<CardDtoToPage> cards;
    private long totalElements;
    private int currentPage;
    private int size;

    public CardPageDto(Page<CardDtoToPage> page) {
        this.cards = page.getContent();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.size = page.getSize();
    }


}
