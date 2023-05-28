package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nufemit.utils.HttpResponseUtils.createOkResponse;

@RestController
@RequestMapping("/registration")
@Tag(description = "Registration related actions", name = "Registration Controller")
@AllArgsConstructor
@Slf4j
public class RegistrationController {

    private RegistrationService registrationService;
    private AuthenticationService authenticationService;

    @Operation(summary = "Register logged User in given Activity")
    @PutMapping("/register/{activityId}")
    public ResponseEntity<ResponseDTO> registerUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                    @PathVariable Long activityId) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(registrationService.registerUser(activityId, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Unregister logged User in given Activity")
    @PutMapping("/unregister/{activityId}")
    public ResponseEntity<ResponseDTO> unregisterUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                      @PathVariable Long activityId) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(registrationService.unregisterUser(activityId, credentialsInfo.getUser()), credentialsInfo);
    }
}
