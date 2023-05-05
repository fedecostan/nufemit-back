package com.nufemit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nufemit.model.Credentials;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Map;

import static java.lang.Boolean.FALSE;

@Component
@AllArgsConstructor
@Slf4j
public class CredentialsUtils {

    private static final String TOKEN_SECRET = "s3Cr3T";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String EXPIRATION = "expiration";

    public static String encrypt(String password) {
        try {
            return toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String createToken(Long id, String email, String password) {
        System.out.println(LocalDateTime.now().plusHours(1));
        return JWT.create()
                .withHeader(Map.of("typ", "JWT", "alg", "HS256"))
                .withClaim(ID, id)
                .withClaim(EMAIL, email)
                .withClaim(PASSWORD, password)
                .withClaim(EXPIRATION, LocalDateTime.now().plusHours(1).toString())
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
    }

    public static Credentials getCredentialsInfo(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            LocalDateTime expirationDate = LocalDateTime.parse(decodedJWT.getClaim(EXPIRATION).asString());
            return Credentials.builder()
                    .id(decodedJWT.getClaim(ID).asLong())
                    .email(decodedJWT.getClaim(EMAIL).asString())
                    .password(decodedJWT.getClaim(PASSWORD).asString())
                    .expiration(expirationDate)
                    .access(verifyExpiration(expirationDate))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Credentials.builder().access(FALSE).build();
        }
    }

    private static boolean verifyExpiration(LocalDateTime expiration) {
        return expiration.isAfter(LocalDateTime.now());
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
