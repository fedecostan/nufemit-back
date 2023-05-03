package com.nufemit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@AllArgsConstructor
public class CredentialsUtils {

    private static final long EXPIRE_TIME = 120 * 60 * 1000;
    private static final String TOKEN_SECRET = "s3Cr3T";

    public static String encrypt(String password) {
        try {
            return toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String createToken(String email, String password) {
        System.out.println(LocalDateTime.now().plusHours(1));
        return JWT.create()
                .withHeader(Map.of("typ", "JWT", "alg", "HS256"))
                .withClaim("email", email)
                .withClaim("password", password)
                .withClaim("expiration", LocalDateTime.now().plusHours(1).toString())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    public static Pair<String, String> getTokenInfo(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String tokenEmail = decodedJWT.getClaim("email").asString();
            String tokenPassword = decodedJWT.getClaim("password").asString();
            return Pair.of(tokenEmail, tokenPassword);
        } catch (Exception e) {
            return Pair.of("null", "null");
        }
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
