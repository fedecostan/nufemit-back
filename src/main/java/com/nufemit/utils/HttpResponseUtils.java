package com.nufemit.utils;

import com.nufemit.model.Credentials;
import com.nufemit.model.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

import static com.nufemit.utils.CredentialsUtils.createToken;

public class HttpResponseUtils {

    public static ResponseEntity<ResponseDTO> createOkResponse(Object o, Credentials credentialsInfo) {
        return ResponseEntity.ok(
                ResponseDTO.builder()
                        .response(o)
                        .token(createToken(
                                credentialsInfo.getId(), credentialsInfo.getEmail(), credentialsInfo.getPassword()))
                        .build());
    }
}
