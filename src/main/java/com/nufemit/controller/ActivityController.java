package com.nufemit.controller;

import com.nufemit.model.Activity;
import com.nufemit.model.Credentials;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.ActivityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.CredentialsUtils.getCredentialsInfo;

@RestController
@RequestMapping("/activities")
@AllArgsConstructor
@Slf4j
public class ActivityController {

    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @RequestBody Activity activity) {
        Credentials credentialsInfo = getCredentialsInfo(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(activityService.createActivity(activity), credentialsInfo);
    }

    private ResponseEntity<ResponseDTO> createOkResponse(Object o, Credentials credentialsInfo) {
        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .response(o)
                        .token(createToken(
                                credentialsInfo.getId(), credentialsInfo.getEmail(), credentialsInfo.getPassword()))
                        .build());
    }
}
