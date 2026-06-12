package com.travelsphere.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET = "TravelSphere_JWT_Secret_Key_Min32Chars_2024";
    private static final long ACCESS_EXPIRATION = 900000L;
    private static final long REFRESH_EXPIRATION = 604800000L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(TEST_SECRET, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
    }

    @Test
    void generateAccessTokenReturnsNonEmptyToken() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "test@email.com", Set.of("ROLE_USER"), "SILVER");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void validateAccessTokenReturnsClaims() {
        UUID userId = UUID.randomUUID();
        String email = "test@email.com";
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        String tier = "GOLD";

        String token = jwtUtil.generateAccessToken(userId, email, roles, tier);
        Jws<Claims> claims = jwtUtil.validateToken(token);

        assertEquals(userId.toString(), claims.getPayload().getSubject());
        assertEquals(email, claims.getPayload().get("email"));
        assertNotNull(claims.getPayload().get("roles"));
        assertEquals(tier, claims.getPayload().get("tier"));
    }

    @Test
    void generateRefreshTokenIsRefreshType() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateRefreshToken(userId);

        assertTrue(jwtUtil.isRefreshToken(token));
    }

    @Test
    void accessTokenIsNotRefreshToken() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "test@email.com", Set.of("ROLE_USER"), "SILVER");

        assertFalse(jwtUtil.isRefreshToken(token));
    }

    @Test
    void getUserIdFromTokenReturnsCorrectUuid() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "test@email.com", Set.of("ROLE_USER"), "SILVER");

        UUID extracted = jwtUtil.getUserIdFromToken(token);
        assertEquals(userId, extracted);
    }

    @Test
    void getTokenIdReturnsNonNull() {
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "test@email.com", Set.of("ROLE_USER"), "SILVER");

        String tokenId = jwtUtil.getTokenId(token);
        assertNotNull(tokenId);
    }

    @Test
    void invalidTokenThrowsException() {
        assertThrows(JwtException.class, () -> jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void tokenWithWrongSecretThrowsException() {
        JwtUtil otherUtil = new JwtUtil("Different_Secret_Key_Min_32_Chars_2024", ACCESS_EXPIRATION, REFRESH_EXPIRATION);
        UUID userId = UUID.randomUUID();
        String token = jwtUtil.generateAccessToken(userId, "test@email.com", Set.of("ROLE_USER"), "SILVER");

        assertThrows(JwtException.class, () -> otherUtil.validateToken(token));
    }

    @Test
    void differentTokensHaveDifferentIds() {
        UUID userId = UUID.randomUUID();
        String t1 = jwtUtil.generateAccessToken(userId, "a@b.com", Set.of("ROLE_USER"), "SILVER");
        String t2 = jwtUtil.generateAccessToken(userId, "a@b.com", Set.of("ROLE_USER"), "SILVER");

        assertNotEquals(jwtUtil.getTokenId(t1), jwtUtil.getTokenId(t2));
    }
}
