package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.User;
import com.nufemit.model.dto.InputValidationDTO;
import com.nufemit.model.dto.LoginDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.UserService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.nufemit.utils.CredentialsUtils.createToken;
import static com.nufemit.utils.HttpResponseUtils.createOkResponse;

@RestController
@RequestMapping("/users")
@Tag(description = "User related actions", name = "User Controller")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    private AuthenticationService authenticationService;

    @Operation(summary = "Get all Users and filter by search box")
    @GetMapping
    public ResponseEntity<ResponseDTO> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                               @RequestParam(required = false) String searchBox) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getUsers(searchBox), credentialsInfo);
    }

    @Operation(summary = "Get logged User id")
    @GetMapping("/id")
    public ResponseEntity<ResponseDTO> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(credentialsInfo.getUser().getId(), credentialsInfo);
    }

    @Operation(summary = "Get User by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getUserById(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getUsersById(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get logged User information")
    @GetMapping("/self")
    public ResponseEntity<ResponseDTO> getLoggedUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getUsersById(credentialsInfo.getUser().getId(), credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get logged User followers")
    @GetMapping("/followers")
    public ResponseEntity<ResponseDTO> getFollowers(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getFollowers(credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get given User followers")
    @GetMapping("/followers/{id}")
    public ResponseEntity<ResponseDTO> getFollowersForUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                           @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getFollowersForUser(id), credentialsInfo);
    }

    @Operation(summary = "Get logged User followings")
    @GetMapping("/following")
    public ResponseEntity<ResponseDTO> getFollowings(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getFollowing(credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get given User followings")
    @GetMapping("/following/{id}")
    public ResponseEntity<ResponseDTO> getFollowings(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.getFollowingForUser(id), credentialsInfo);
    }

    @Operation(summary = "Update logged User information")
    @PutMapping
    public ResponseEntity<ResponseDTO> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @RequestBody User user) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        InputValidationDTO inputValidationDTO = userService.updateUser(user, credentialsInfo.getUser());
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

    @Operation(summary = "Update logged User image")
    @PutMapping("/image")
    public ResponseEntity<ResponseDTO> updateImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @RequestBody User user) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.updateUserImage(user, credentialsInfo.getUser().getId()), credentialsInfo);
    }

    @Operation(summary = "Create new User")
    @PostMapping
    public ResponseEntity<InputValidationDTO> create(@RequestBody User user) {
        InputValidationDTO inputValidationDTO = userService.createUser(user);
        if (inputValidationDTO.getErroredFields().isEmpty()) {
            return ResponseEntity.ok(inputValidationDTO);
        } else {
            return new ResponseEntity<>(inputValidationDTO, HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Operation(summary = "Validate Login credentials")
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(userService.loginUser(loginDTO));
    }

    @Operation(summary = "Follow given User")
    @PutMapping("/follow/{id}")
    public ResponseEntity<ResponseDTO> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                  @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.followUser(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Unfollow given User")
    @PutMapping("/unfollow/{id}")
    public ResponseEntity<ResponseDTO> unfollowUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                    @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.unfollowUser(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Delete logged User and all its Activities")
    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return createOkResponse(userService.deleteUser(credentialsInfo.getUser()), credentialsInfo);
    }
}
