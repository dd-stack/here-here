package com.example.Here.domain.auth.hendler;

import com.example.Here.domain.auth.KakaoUserInfo;
import com.example.Here.domain.auth.OAuth2UserInfo;
import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.repository.RefreshTokenRepository;
import com.example.Here.domain.member.entity.Member;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.domain.member.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class Oauth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;


    public Oauth2MemberSuccessHandler(MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;}

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        OAuth2UserInfo oAuth2UserInfo = null;

        oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes());

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String nickname = oAuth2UserInfo.getName();

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if(optionalMember.isEmpty()) {

            Member newMember = saveMember(email, nickname);
            redirect(request, response, newMember);
        }

        else {

           Member existedMember = optionalMember.get();

           if(!Objects.equals(existedMember.getNickName(), nickname)) {
               Member updatedMember = updateMember(existedMember, nickname);
               redirect(request, response, updatedMember);
           }

           else redirect(request, response, existedMember);

        }

    }

    private Member saveMember(String email, String nickname){

        Member member = new Member();

        member = member.builder()
                    .email(email)
                    .nickName(nickname)
                    .build();

       return memberRepository.save(member);

    }

    private Member updateMember(Member member, String nickname){

        member.setNickName(nickname);

      return memberRepository.save(member);

    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, Member member) throws IOException {

        String accessToken = delegateAccessToken(member);
        String refreshToken =  delegateRefreshToken(member);

        String uri = createURI(accessToken, refreshToken).toString();

        log.info(accessToken);
        log.info(refreshToken);

        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String delegateAccessToken(Member member) {
        String accessToken = jwtTokenProvider.generateAccessToken(member);
        return "Bearer " + accessToken;
    }

    private String delegateRefreshToken(Member member) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        refreshTokenRepository.save(member.getEmail(), refreshToken);
        return refreshToken;
    }

    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(3000)
                .path("/")
                .queryParams(queryParams)
                .build()
                .toUri();
    }

}
