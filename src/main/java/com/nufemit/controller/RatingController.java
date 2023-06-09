package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.dto.RatingDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static com.nufemit.utils.HttpResponseUtils.createOkResponse;

@RestController
@RequestMapping("/ratings")
@Tag(description = "Rating related actions", name = "Rating Controller")
@AllArgsConstructor
@Slf4j
public class RatingController {

    private RatingService ratingService;
    private AuthenticationService authenticationService;

    @Operation(summary = "Create new Rating")
    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody RatingDTO ratingDTO) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(ratingService.createRating(ratingDTO, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Delete Rating by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(ratingService.deleteRating(id, credentialsInfo.getUser()), credentialsInfo);
    }
}
