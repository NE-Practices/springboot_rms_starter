package com.spring.rms.payload.request;

import com.spring.rms.enums.ECategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateResourceDTO {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private ECategory category;
}