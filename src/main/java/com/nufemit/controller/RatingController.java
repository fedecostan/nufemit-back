package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.Rating;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.RatingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
@AllArgsConstructor
@Slf4j
public class RatingController {

    private RatingService ratingService;
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @RequestBody Rating rating) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(ratingService.createRating(rating, credentialsInfo.getUser()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.ok(ratingService.deleteRating(id, credentialsInfo.getUser()));
    }
}
