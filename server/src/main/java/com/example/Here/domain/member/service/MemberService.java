package com.example.Here.domain.member.service;


import com.example.Here.domain.auth.service.AuthenticationService;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.processor.MemberProcessor;
import com.example.Here.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberProcessor memberProcessor;

    private final AuthenticationService authenticationService;

    private final CardRepository cardRepository;

    private final InvitationRepository invitationRepository;

    @Transactional
    public Member getOrCreateMember(String email, String nickname, String profileImageURL) {

        Optional<Member> optionalMember = memberRepository.findByEmailIncludingDeleted(email);

        if (optionalMember.isPresent()) {
            Member existingMember = optionalMember.get();
            existingMember = memberProcessor.updateMemberInfo(existingMember, nickname, profileImageURL);
            return memberRepository.save(existingMember);
        } else {
            Member newMember = memberProcessor.createNewMember(email, nickname, profileImageURL);
            return memberRepository.save(newMember);
        }
    }

    @Transactional
    public void logout() {

        Member member = authenticationService.getAuthenticatedMember();
        memberProcessor.removeTokens(member);

    }

    @Transactional
    public void deleteMember() {

        Member member = authenticationService.getAuthenticatedMember();
        member.setDeleted(true);

        cardRepository.updateDeletedStatusForCardsCreatedByMember(member, true);
        invitationRepository.updateDeletedStatusForInvitationsReceivedByMember(member, true);

    }

}

