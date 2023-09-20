package com.example.Here.domain.invitation.Service;

import com.example.Here.domain.auth.service.AuthenticationService;
import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.invitation.processor.InvitationProcessor;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.dto.MemberDtoToAcceptList;
import com.example.Here.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvitationService {

    private final InvitationRepository invitationRepository;

    private final AuthenticationService authenticationService;

    private final InvitationProcessor invitationProcessor;

    @Transactional
    public void acceptInvitation(String cardId) {

        Member member = authenticationService.getAuthenticatedMember();
        Card card = invitationProcessor.findCardById(cardId);

        invitationProcessor.checkCardAlreadyAccepted(member, card);
        invitationProcessor.makeNewInvitation(member, card);

    }

    @Transactional
    public void deleteInvitation(String cardId) {
        Member member = authenticationService.getAuthenticatedMember();
        Invitation deleteInvitation = invitationProcessor.findInvitationByCardIdAndReceiver(cardId, member);

        invitationProcessor.validateMemberPermission(deleteInvitation, member);

        deleteInvitation.setDeleted(true);
        invitationRepository.save(deleteInvitation);
    }

    @Transactional
    public List<MemberDtoToAcceptList> getAcceptedMembersByCardId(String cardId) {
        Member member = authenticationService.getAuthenticatedMember();
        Card card = invitationProcessor.findCardById(cardId);

        invitationProcessor.validateViewPermission(card, member);

        return invitationProcessor.findAcceptedMembersByCardId(cardId);
    }

}
