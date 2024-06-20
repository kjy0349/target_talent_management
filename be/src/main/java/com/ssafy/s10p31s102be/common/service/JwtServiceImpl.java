package com.ssafy.s10p31s102be.common.service;

import com.ssafy.s10p31s102be.common.exception.TokenInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService{
    String JWT_SECRETKEY = "abcdefghijklmnopqrstuvwxyzwntjrdbsbabomuncheongi";

    @Override
    public String isValidToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRETKEY.getBytes(StandardCharsets.UTF_8));
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            /*
                토큰이 만료되지 않았다면, 토큰 내 Member id 반환
                토큰이 만료되었다면, null 반환
             */
            if (!claims.getPayload().getExpiration().before(new Date())) {
                return extractMemberId(claims);
            } else{
                return null;
            }
        } catch (JwtException e) {
            throw new TokenInvalidException(this);
        }
    }

    @Override
    public List<String> generateToken(String knoxId) {
        String accessToken = createAccessToken(knoxId);

        List<String> tokens = new ArrayList<>();
        tokens.add(accessToken);

        return tokens;
    }

    private String createAccessToken(String knoxId) {
        Date date = new Date();
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRETKEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .issuer("SAMSUNG")
                .claim("knoxId", knoxId)
                .issuedAt(date)
                .expiration(new Date(date.getTime() + (1000 * 60 * 60 * 24)))  // 24시간 설정
                .signWith(key).compact();
    }

    /*
        인증된 토큰의 payload에서 userId를 추출해내는 Method
     */
    private String extractMemberId(Jws<Claims> claims) {
        return claims.getPayload().get("knoxId", String.class);
    }
}
