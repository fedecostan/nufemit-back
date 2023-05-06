package com.nufemit.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Credentials {
    private User user;
    private LocalDateTime expiration;
    private boolean access;
}
