package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.model.dto.MessageDTO;
import com.nufemit.model.dto.ResponseDTO;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.MessageService;
import com.nufemit.utils.HttpResponseUtils;
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
@AllArgsConstructor
@Slf4j
public class MessageController {

    private MessageService messageService;
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getConversations(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.getConversations(credentialsInfo.getUser()), credentialsInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getMessages(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                   @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.getMessages(id, credentialsInfo.getUser()), credentialsInfo);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> send(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                            @RequestBody MessageDTO messageDTO) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.sendMessage(messageDTO, credentialsInfo.getUser()), credentialsInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                              @PathVariable Long id) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return HttpResponseUtils.createOkResponse(messageService.deleteMessage(id, credentialsInfo.getUser()), credentialsInfo);
    }
}
