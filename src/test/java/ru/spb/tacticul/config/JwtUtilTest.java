package ru.spb.tacticul.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String secret = "a8a09789d69a0fc5038c616f0f792a0b6268a33983e3ac6b24b2a6c8470ebc2ed43a8ca124f2b558361f34feaf09bc83105956803299c83cd4ac3c5e9691daea1fe604dad5d5e1eeeb8b6d268740ea2e21b67cab2ed9fe6b527842d3511a58eec4ea49b9b2cc525345c7d6ccb92753ca6f53a2665bb85ead8c5881b1481130926972c3370424e7cbe683571b21274c28cf523fda8b7a73ef9a82583c5c14847e540d6787b40fcd02e9a5f09087e80d33a2de267c40a8d9c9da472623e264894552ac021b6c298e43230211c59ff831640289fff18f42834b79b2f74cdfa58e3d4247947cc5265c704f6452667d4ce58e9591d81e6253a9b1a9b3439d1806d641";
    private final long jwtExpirationMs = 1000 * 60 * 5;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secret, jwtExpirationMs);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken("testUser");
        String username = jwtUtil.extractUsername(token);
        assertEquals("testUser", username);
    }

    @Test
    void testValidateToken() {
        String token = jwtUtil.generateToken("testUser");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void testExpiredToken() throws InterruptedException {
        JwtUtil shortLivedJwtUtil = new JwtUtil(secret, 1);
        String token = shortLivedJwtUtil.generateToken("testUser");
        Thread.sleep(10);
        assertFalse(shortLivedJwtUtil.validateToken(token));
    }
}
