package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.User;
import com.nufemit.model.dto.LoginDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.nufemit.utils.HttpResponseUtils.createOkResponse;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestParam(required = false) String searchBox) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getUsers(searchBox), credentialsInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getUsersById(id), credentialsInfo);
    }

    @PostMapping
    public ResponseEntity<Boolean> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.loginUser(loginDTO));
    }
}
