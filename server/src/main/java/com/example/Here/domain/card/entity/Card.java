package com.example.Here.domain.card.entity;

import com.example.Here.global.audit.BaseTime;
import jakarta.persistence.*;
import lombok.Setter;

@Entity
@Setter
public class Card extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String title;

    private String background;
    //이미지URL 혹은 배경색깔

    private String imageUrl;

    @Column(nullable = true, length = 150)
    private String content;

    private String textLocation;

    private String textColor;

    @Column(nullable = true)
    private String location;

}
