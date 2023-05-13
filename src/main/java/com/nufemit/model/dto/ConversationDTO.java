package com.nufemit.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ConversationDTO {
    private Long conversationId;
    private Long userId;
    private String userProfileImage;
    private String conversationUser;
    private String lastMessage;
    private Boolean unread;
    private LocalDateTime date;
}
