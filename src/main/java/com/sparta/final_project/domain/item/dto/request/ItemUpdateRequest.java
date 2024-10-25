package com.sparta.final_project.domain.item.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NotBlank
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {
    private String name;
    private String description;
    private String imageUrl;
}
