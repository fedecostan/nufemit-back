package com.nufemit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class CredentialsUtils {

    public static final String TOKEN_SECRET = "s3Cr3T";
    public static final String ID = "id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String EXPIRATION = "expiration";

    public static String encrypt(String password) {
        try {
            return toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String createToken(Long id, String email, String password) {
        return JWT.create()
            .withHeader(Map.of("typ", "JWT", "alg", "HS256"))
            .withClaim(ID, id)
            .withClaim(EMAIL, email)
            .withClaim(PASSWORD, password)
            .withClaim(EXPIRATION, LocalDateTime.now().plusHours(1).toString())
            .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    private static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    private static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
