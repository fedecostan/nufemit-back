package com.nufemit.controller;

import com.nufemit.model.Activity;
import com.nufemit.model.Credentials;
import com.nufemit.model.dto.InputValidationDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.ActivityService;
import com.nufemit.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.HttpResponseUtils.createOkResponse;

@RestController
@RequestMapping("/activities")
@Tag(description = "Activity related actions", name = "Activity Controller")
@AllArgsConstructor
@Slf4j
public class ActivityController {

    private ActivityService activityService;
    private AuthenticationService authenticationService;

    @Operation(summary = "Get all Activities and filter by search box")
    @GetMapping
    public ResponseEntity<ResponseDTO> getActivity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @RequestParam(required = false) String searchBox) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.getActivities(searchBox), credentialsInfo);
    }

    @Operation(summary = "Get next Activity for logged User")
    @GetMapping("/next")
    public ResponseEntity<ResponseDTO> getUserNextActivity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.getUserNextActivity(credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get Activity by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getActivityById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.getActivitiesById(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get last 5 Activities for logged User")
    @GetMapping("/recent")
    public ResponseEntity<ResponseDTO> getRecentActivities(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.getRecentActivities(credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get last 5 Activities for given User")
    @GetMapping("/recent/{id}")
    public ResponseEntity<ResponseDTO> getRecentActivitiesForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                                  @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.getRecentActivitiesForUser(id), credentialsInfo);
    }

    @Operation(summary = "Create new Activity")
    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody Activity activity) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        InputValidationDTO inputValidationDTO = activityService.createActivity(activity, credentialsInfo.getUser());
        if (inputValidationDTO.getErroredFields().isEmpty()) {
            return createOkResponse(inputValidationDTO, credentialsInfo);
        } else {
            return new ResponseEntity<>(ResponseDTO.builder()
                .response(inputValidationDTO)
                .token(createToken(
                    credentialsInfo.getUser().getId(),
                    credentialsInfo.getUser().getEmail(),
                    credentialsInfo.getUser().getPassword()))
                .loggedId(credentialsInfo.getUser().getId())
                .build(), HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Operation(summary = "Delete Activity by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.deleteActivity(id, credentialsInfo.getUser()), credentialsInfo);
    }
}
