package com.example.Here.domain.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class CardDto {

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String background;
    //이미지URL 혹은 배경색깔

    private String content;

    private String textLocation;

    private String textColor;

    private String location;

    private String creatorEmail;


    public CardDto(String title, LocalDateTime startTime, LocalDateTime endTime, String background, String content, String textLocation, String textColor, String location, String creatorEmail){
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.background = background;
        this.content = content;
        this.textLocation = textLocation;
        this.textColor = textColor;
        this.location = location;
        this.creatorEmail = creatorEmail;
    }

    @Getter
    @Setter
    public static class Response{

        private String id;

        public Response(){

        }

        public Response(String id) {
            this.id = id;
        }


    }


}
