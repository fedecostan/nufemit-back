package com.nufemit.model.dto;

import com.nufemit.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private User user;
    private Integer activities;
    private Integer followers;
    private Integer following;
    private Double rating;
    private Boolean followedByUser;
}
