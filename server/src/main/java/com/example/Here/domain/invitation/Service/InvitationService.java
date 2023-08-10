package com.example.Here.domain.invitation.Service;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final MemberRepository memberRepository;

    private final CardRepository cardRepository;

    public InvitationService(InvitationRepository invitationRepository, MemberRepository memberRepository, CardRepository cardRepository) {
        this.invitationRepository = invitationRepository;
        this.memberRepository = memberRepository;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void acceptInvitation(String cardId, Member member) {

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));

        //이미 수락한 초대장인지 확인
        List<Card> receivedCards = invitationRepository.findByReceiver(member).stream().map(Invitation::getCard).toList();
        if (receivedCards.contains(card)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_ACCEPTED);
        }

        Invitation invitation = new Invitation();
        invitation.setReceiver(member);
        invitation.setCard(card);

        invitationRepository.save(invitation);
    }

    @Transactional
    public void deleteInvitation(String cardId, Member member) {

        Invitation deleteInvitation = invitationRepository.findByCardIdAndReceiver(cardId, member)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVITATION_NOT_FOUND));

        String receiverEmail = deleteInvitation.getReceiver().getEmail();
        String memberEmail = member.getEmail();

        if(receiverEmail.equals(memberEmail)) {
            deleteInvitation.setDeleted(true);
            invitationRepository.save(deleteInvitation);
        }

        else throw new BusinessLogicException(ExceptionCode.MEMBER_NO_PERMISSION);


    }

}
