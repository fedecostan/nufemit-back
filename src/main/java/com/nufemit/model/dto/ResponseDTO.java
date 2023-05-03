package com.nufemit.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private Object response;
    private String token;
}
