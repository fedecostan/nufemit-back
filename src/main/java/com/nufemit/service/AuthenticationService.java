package com.nufemit.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nufemit.model.Credentials;
import com.nufemit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.nufemit.utils.CredentialsUtils.EMAIL;
import static com.nufemit.utils.CredentialsUtils.EXPIRATION;
import static com.nufemit.utils.CredentialsUtils.ID;
import static com.nufemit.utils.CredentialsUtils.PASSWORD;
import static com.nufemit.utils.CredentialsUtils.TOKEN_SECRET;
import static java.lang.Boolean.FALSE;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {

    private UserRepository userRepository;

    public Credentials getCredentials(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            LocalDateTime expirationDate = LocalDateTime.parse(decodedJWT.getClaim(EXPIRATION).asString());
            Long id = decodedJWT.getClaim(ID).asLong();
            String email = decodedJWT.getClaim(EMAIL).asString();
            String password = decodedJWT.getClaim(PASSWORD).asString();
            boolean present = userRepository.findByIdAndEmailAndPassword(id, email, password).isPresent();
            return Credentials.builder()
                .id(id)
                .email(email)
                .password(password)
                .expiration(expirationDate)
                .access(present && verifyExpiration(expirationDate))
                .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Credentials.builder().access(FALSE).build();
        }
    }

    private boolean verifyExpiration(LocalDateTime expiration) {
        return expiration.isAfter(LocalDateTime.now());
    }
}
