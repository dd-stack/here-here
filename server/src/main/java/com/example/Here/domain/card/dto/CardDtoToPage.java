package com.example.Here.domain.card.dto;

import com.example.Here.domain.card.entity.Card;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CardDtoToPage {

    private String id;

    private String title;

    private String startTime;

    private String endTime;

    private String location;

    public CardDtoToPage(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.startTime = card.getStartTime();
        this.endTime = card.getEndTime();
        this.location = card.getLocation();
    }

}
