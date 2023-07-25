package com.example.Here.domain.auth.jwt;

import com.example.Here.domain.auth.repository.RefreshTokenRepository;
import com.example.Here.global.exception.SecurityAuthException;
import com.example.Here.global.exception.SecurityAuthExceptionCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Claims accessTokenClaims = verifyJws(request, response);
            if (accessTokenClaims != null) {
                setAuthenticationToContext(accessTokenClaims);
            }
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (SecurityAuthException e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer");
    }

    private Claims verifyJws(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = request.getHeader("Authorization").substring(7);

        if (refreshTokenRepository.findBy(accessToken) != null) {
            throw new SecurityAuthException(SecurityAuthExceptionCode.MEMBER_LOGOUT);
        }

        return jwtTokenProvider.parseClaims(accessToken);
    }

    private void setAuthenticationToContext(Claims claims) {
        String email = (String) claims.get("email");

        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.authenticated(email, null, null);

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}