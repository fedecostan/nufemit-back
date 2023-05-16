package com.nufemit.model.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InputValidationDTO {
    private List<String> erroredFields;
    private String reason;

    public InputValidationDTO() {
        this.erroredFields = new ArrayList<>();
        this.reason = "";
    }

    public void addError(String field, String reason) {
        this.erroredFields.add(field);
        this.reason += "- " + reason + "<br>";
    }
}
