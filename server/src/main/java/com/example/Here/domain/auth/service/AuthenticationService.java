package com.example.Here.domain.auth.service;

import com.example.Here.domain.member.entity.Member;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public Member getAuthenticatedMember() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.MEMBER_NO_PERMISSION);
        }

        return (Member) authentication.getPrincipal();
    }

}
