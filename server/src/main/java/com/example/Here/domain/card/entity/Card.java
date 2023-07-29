package com.example.Here.domain.card.entity;

import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.audit.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Card extends BaseTime {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;

    @Column(nullable = false, length = 20)
    private String title;

    private String startTime;

    private String endTime;

    private String background;
    //이미지URL 혹은 배경색깔

    @Column(nullable = true, length = 150)
    private String content;

    private String textLocation;

    private String textColor;

    @Column(nullable = true)
    private String location;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Member creator;

    @OneToMany(mappedBy = "card")
    private List<Invitation> invitations = new ArrayList<>();


    @Builder
    public Card(String title, String startTime, String endTime, String background, String content, String textLocation, String textColor, String location) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.background = background;
        this.content = content;
        this.textLocation = textLocation;
        this.textColor = textColor;
        this.location = location;
    }

}
