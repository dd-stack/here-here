package com.example.Here.domain.member.entity;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.global.audit.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "deleted = false")
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String profileImageURL;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Card> createdCards = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Invitation> receivedInvitations = new ArrayList<>();

    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    public Member(String email, String nickName, String profileImageURL){
        this.email = email;
        this.nickName = nickName;
        this.profileImageURL = profileImageURL;
    }

}
