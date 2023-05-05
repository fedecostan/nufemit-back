package com.nufemit.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Credentials {
    private Long id;
    private String email;
    private String password;
    private LocalDateTime expiration;
    private boolean access;
}
