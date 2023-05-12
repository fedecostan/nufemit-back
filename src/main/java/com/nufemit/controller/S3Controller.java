package com.nufemit.controller;

import com.nufemit.model.Credentials;
import com.nufemit.service.AuthenticationService;
import com.nufemit.service.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resources")
@AllArgsConstructor
@Slf4j
public class S3Controller {

    private AuthenticationService authenticationService;
    private S3Service s3Service;

    @PostMapping("/{filename}")
    public ResponseEntity<Boolean> uploadFile(@RequestPart("file") MultipartFile file,
                                              @PathVariable String filename) {
        return ResponseEntity.ok(s3Service.uploadFile(file, filename));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<ByteArrayResource> getFile(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @PathVariable String filename) {
        Credentials credentialsInfo = authenticationService.getCredentials(token);
        if (!credentialsInfo.isAccess()) return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        return s3Service.getFile(filename);
    }
}
