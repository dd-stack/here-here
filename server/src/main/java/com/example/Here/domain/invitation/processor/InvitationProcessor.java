package com.example.Here.domain.invitation.processor;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.dto.MemberDtoToAcceptList;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InvitationProcessor {

    private final InvitationRepository invitationRepository;

    private final CardRepository cardRepository;

    public void checkCardAlreadyAccepted(Member member, Card card) {

        Optional<Invitation> optionalInvitation = invitationRepository.findByReceiverAndCard(member, card);
        if (optionalInvitation.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_ACCEPTED);
        }
    }

    public void makeNewInvitation(Member member, Card card) {

        Invitation invitation = new Invitation();
        invitation.setReceiver(member);
        invitation.setCard(card);

        invitationRepository.save(invitation);
    }

    public Invitation findInvitationByCardIdAndReceiver(String cardId, Member member) {
        return invitationRepository.findByCardIdAndReceiver(cardId, member)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INVITATION_NOT_FOUND));
    }

    public void validateMemberPermission(Invitation invitation, Member member) {

        String receiverEmail = invitation.getReceiver().getEmail();
        String memberEmail = member.getEmail();

        if (!receiverEmail.equals(memberEmail)) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.MEMBER_NO_PERMISSION);
        }
    }

    public List<MemberDtoToAcceptList> findAcceptedMembersByCardId(String cardId) {

        List<Invitation> invitations = invitationRepository.findByCardId(cardId);
        return invitations.stream()
                .map(invitation -> {
                    Member acceptMember = invitation.getReceiver();
                    MemberDtoToAcceptList dto = new MemberDtoToAcceptList();
                    dto.setNickname(acceptMember.getNickName());
                    dto.setProfileImageURL(acceptMember.getProfileImageURL());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Card findCardById(String cardId) {

        return cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CARD_NOT_FOUND));
    }

    public void validateViewPermission(Card card, Member member) {

        String loginEmail = member.getEmail();
        String creatorEmail = card.getCreator().getEmail();

        if (!loginEmail.equals(creatorEmail)) {
            throw new BusinessLogicException(ExceptionCode.NO_PERMISSION_FOR_CHECKING_MEMBER);
        }
    }

}
