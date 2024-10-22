package com.sparta.final_project.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemSaveResponse {
    private Long id; // 생성된 아이템 ID
    private String itemName; // 상품명
    private String itemDescription; // 상품 설명
    private String itemUrl; // 상품 URL
    private Long userId; // 사용자 ID
}
