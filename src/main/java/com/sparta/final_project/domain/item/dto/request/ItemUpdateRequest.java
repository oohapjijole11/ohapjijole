package com.sparta.final_project.domain.item.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemUpdateRequest {
    private String itemName;
    private String itemDescription;
    private MultipartFile file; // 업로드할 파일 (선택적)

}
