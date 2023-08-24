package com.example.Here.domain.member.service;


import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.global.exception.BusinessLogicException;
import com.example.Here.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member getOrCreateMember(String email, String nickname, String profileImageURL) {

        // 이메일을 통해 Member가 존재하는지 확인
        Optional<Member> optionalMember = memberRepository.findByEmailIncludingDeleted(email);

        if (optionalMember.isPresent()) {

            // Member가 존재한다면 닉네임이 업데이트 되었는지 확인
            Member existingMember = optionalMember.get();

            // 닉네임이 업데이트 되었다면 해당 정보를 변경하고 저장
            if (!existingMember.getNickName().equals(nickname)) {
                existingMember.setNickName(nickname);
            }

            // 프로필 이미지가 업데이트 되었다면 해당 정보를 변경하고 저장
            if (!Objects.equals(existingMember.getProfileImageURL(), profileImageURL)) {
                existingMember.setProfileImageURL(profileImageURL);
            }

            // 탈퇴 처리한 회원 다시 활성화
            if(existingMember.isDeleted()) {
                existingMember.setDeleted(false);
            }

            return memberRepository.save(existingMember);

        } else {
            // Member가 존재하지 않는다면 새 Member를 생성하고 저장
            Member newMember = Member.builder()
                    .email(email)
                    .nickName(nickname)
                    .profileImageURL(profileImageURL)
                    .build();
            return memberRepository.save(newMember);
        }
    }


}
