package com.nufemit.model.dto;

import com.nufemit.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ActivityDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private String place;
    private LocalDateTime dateTime;
    private Double price;
    private Boolean joined;
    private Integer limit;
    private List<User> joiners;
}
