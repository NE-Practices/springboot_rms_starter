package com.spring.rms.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateResourceDTO {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String category;
}