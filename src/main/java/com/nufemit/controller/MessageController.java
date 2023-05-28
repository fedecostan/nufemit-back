package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.dto.NewMessageDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.MessageService;
import com.nufemit.utils.HttpResponseUtils;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
@Tag(description = "Message related actions", name = "Message Controller")
@AllArgsConstructor
@Slf4j
public class MessageController {

    private MessageService messageService;
    private AuthenticationService authenticationService;

    @Operation(summary = "Get all Conversations for logged user")
    @GetMapping
    public ResponseEntity<ResponseDTO> getConversations(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.getConversations(credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Get Messages for given Conversation")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.getMessages(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Save new Message sent")
    @PostMapping
    public ResponseEntity<ResponseDTO> send(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @RequestBody NewMessageDTO newMessageDTO) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.sendMessage(newMessageDTO, credentialsInfo.getUser()), credentialsInfo);
    }

    @Operation(summary = "Delete Message by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.deleteMessage(id, credentialsInfo.getUser()), credentialsInfo);
    }
}
