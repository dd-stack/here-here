package com.example.Here.domain.member.entity;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.global.audit.BaseTime;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickName;

    @OneToMany(mappedBy = "creator")
    private List<Card> createdCards = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<Invitation> receivedInvitations = new ArrayList<>();

    @Builder
    public Member(String email, String nickName){
        this.email = email;
        this.nickName = nickName;
    }



}
