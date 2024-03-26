package com.trendytracker.common.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.security.Key;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.trendytracker.Common.Config.JwtProvider;
import com.trendytracker.Domain.AppService.Users.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.security.Keys;


@SpringBootTest
public class JwtProviderTest {
    @InjectMocks
    private JwtProvider jwtProvider;
    @Mock
    private User mockUser;

    private String secretKey = "trendy-tracker-test-secret-key-how-change-the-world";
    private String testEmail = "test@example.com";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtProvider = new JwtProvider(secretKey);
        
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getEmail()).thenReturn(testEmail);
    }

    @Test
    public void testCreateToken() {
        // given
        long currentTimeMillis = 1609459200000L; // 2021-01-01 00:00:00
        Date fixedNow = new Date(currentTimeMillis);

        long expirationTimeMillis = 1611014400000L; // 2021-01-20 00:00:00
        Date fixedExpiration = new Date(expirationTimeMillis);

        JwtProvider jwtProviderSpy = Mockito.spy(jwtProvider);
        when(jwtProviderSpy.getCurrentTime()).thenReturn(fixedNow);
        when(jwtProviderSpy.calculateExpirationDate(fixedNow)).thenReturn(fixedExpiration);

        // when
        String token = jwtProviderSpy.createToken(mockUser);

        // then
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        JwtBuilder expectedJwt = io.jsonwebtoken.Jwts.builder()
                .setSubject("trendy-tracker")
                .setIssuedAt(fixedNow)
                .setExpiration(fixedExpiration)
                .claim("user_id", 1L)
                .claim("email", "test@example.com")
                .signWith(key);

        assertEquals(expectedJwt.compact(), token);
    }

    @Test
    public void testDecodeJwt() {
        // given
        String token = jwtProvider.createToken(mockUser);

        // when
        Claims claims = jwtProvider.decodeJwt(token);

        // then 
        assertEquals("trendy-tracker", claims.getSubject());
        assertEquals(1L, claims.get("user_id", Long.class).longValue());
        assertEquals("test@example.com", claims.get("email", String.class));
    }
}
