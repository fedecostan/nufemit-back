package com.nufemit.model.dto;

import lombok.Data;

@Data
public class NewMessageDTO {
    private Long receiverId;
    private String message;
}
