package com.sparta.final_project.domain.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateRequest {
    private String name;
    private String description;
    private List<String> imageUrls;
}