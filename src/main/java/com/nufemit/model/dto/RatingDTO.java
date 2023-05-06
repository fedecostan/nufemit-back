package com.nufemit.model.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long id;
    private Integer rating;
    private String comment;
}
