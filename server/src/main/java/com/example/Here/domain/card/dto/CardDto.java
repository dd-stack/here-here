package com.example.Here.domain.card.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CardDto {

    private String title;

    private String startTime;

    private String endTime;

    private String background;
    //이미지URL 혹은 배경색깔

    private String content;

    private String textLocation;

    private String textColor;

    private String location;

    private String creatorEmail;


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
