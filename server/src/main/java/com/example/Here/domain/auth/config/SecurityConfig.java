package com.example.Here.domain.auth.config;

import com.example.Here.domain.auth.hendler.Oauth2MemberSuccessHandler;
import com.example.Here.domain.auth.jwt.JwtTokenProvider;
import com.example.Here.domain.auth.repository.RefreshTokenRepository;
import com.example.Here.domain.member.repository.MemberRepository;
import com.example.Here.domain.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberService memberService;



    public SecurityConfig(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();

    }

}
