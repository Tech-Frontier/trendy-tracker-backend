package com.trendyTracker.common.config;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.trendyTracker.Appservice.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
    // TODO 분산 시스템을 고려하면 Redis 로 변환 필요

    @Value("${jwt.secret_key}")
    private String secret_key;

    public JwtProvider(@Value("${jwt.secret_key}") String secret_key){
        this.secret_key = secret_key;
    }

    public String createToken(User user){
        Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
        String subject = "trendy-tracker";

        Date now = getCurrentTime();
        Date expiration = calculateExpirationDate(now);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .claim("user_id", user.getId())
                .claim("email", user.getEmail())
                .compact();
    }

    protected Date getCurrentTime(){
    /*
     * 현재 시간을 반환 for TestCode
     */
        return new Date();
    }

    protected Date calculateExpirationDate(Date now){
    /*
     *  만료 날짜를 15일 (24시간 x 60분 x 60초 x 1000밀리초) 후로 설정
     */
        long expirationTimeMillis = now.getTime() + (15L * 24 * 60 * 60 * 1000);
        return new Date(expirationTimeMillis);
    }


    public Claims decodeJwt(String jwt){
        try{
            Key key = Keys.hmacShaKeyFor(secret_key.getBytes());
            return Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(jwt)
                                .getBody();
        }
        catch (ExpiredJwtException ex){
            throw new JwtException("JWT verification failed: " + ex.getMessage());
        }
        catch (SecurityException ex){
            throw new JwtException("JWT verification failed: " + ex.getMessage());
        }   
        catch (UnsupportedJwtException ex){
            throw new JwtException("JWT verification failed: " + ex.getMessage());
        }
    }
}
