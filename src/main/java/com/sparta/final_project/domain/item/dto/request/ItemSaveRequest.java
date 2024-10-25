package com.sparta.final_project.domain.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemSaveRequest {
    private String itemName;
    private String itemDescription;
    private List<String> itemUrls; // 이미지 URL 목록
}
