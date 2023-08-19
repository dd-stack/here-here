package com.example.Here.domain.card.dto;

import com.example.Here.domain.card.entity.Card;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CardDtoListToPage {

    private String id;

    private String title;

    private String startTime;

    private String endTime;

    private String location;

    public CardDtoListToPage(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.startTime = card.getStartTime();
        this.endTime = card.getEndTime();
        this.location = card.getLocation();
    }

    @Override
    public String toString() {
        return "CardDtoListToPage{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

}
