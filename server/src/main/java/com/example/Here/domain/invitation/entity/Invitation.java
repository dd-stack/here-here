package com.example.Here.domain.invitation.entity;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.audit.BaseTime;
import jakarta.persistence.*;

@Entity
public class Invitation extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;
}
