package com.spring.rms.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateResourceDTO {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String category;
}