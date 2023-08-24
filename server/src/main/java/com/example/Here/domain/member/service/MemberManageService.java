package com.example.Here.domain.member.service;

import com.example.Here.domain.auth.service.KakaoAuthService;
import com.example.Here.domain.auth.service.RedisService;
import com.example.Here.domain.card.repository.CardRepository;
import com.example.Here.domain.invitation.repository.InvitationRepository;
import com.example.Here.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberManageService {

    private final KakaoAuthService kakaoAuthService;

    private final CardRepository cardRepository;

    private final InvitationRepository invitationRepository;

    private final RedisService redisService;

    public void logout(String email) {

        // refresh token을 삭제합니다.
        redisService.removeRefreshToken(email);

        // 카카오 토큰을 무효화합니다.
        kakaoAuthService.logoutKakaoToken(email);

        // 카카오 토큰을 삭제합니다.
        redisService.removeKakaoToken(email);

        // SecurityContext를 초기화합니다.
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void deleteMember(Member member) {
        member.setDeleted(true);
        cardRepository.updateDeletedStatusForCardsCreatedByMember(member, true);
        invitationRepository.updateDeletedStatusForInvitationsReceivedByMember(member, true);

    }
}
