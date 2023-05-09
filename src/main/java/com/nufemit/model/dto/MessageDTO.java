package com.nufemit.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageDTO {
    private String flow;
    private String message;
    private LocalDateTime dateTime;
}
