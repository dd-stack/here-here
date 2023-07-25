package com.example.Here.domain.auth.jwt;

import com.example.Here.domain.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private Key key;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;


    public JwtTokenProvider(@Value("${jwt.key}") String secretKey){

        String base64Key = Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64Key);

        this.key = Keys.hmacShaKeyFor(keyBytes);

    }


    public String generateAccessToken(Member member){
        return buildToken(member, accessTokenExpirationMinutes);
    }

    public String generateRefreshToken(Member member){
        return buildToken(member, refreshTokenExpirationMinutes);
    }

    private String buildToken(Member member, int expirationMinutes){
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expirationMinutes);

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", member.getEmail());
        claims.put("nickname", member.getNickName());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(Timestamp.valueOf(expiryTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String jws){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws)
                .getBody();
    }
}
