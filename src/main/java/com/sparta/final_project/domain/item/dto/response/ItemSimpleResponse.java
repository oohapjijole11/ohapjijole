package com.sparta.final_project.domain.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class ItemSimpleResponse {
    private Long id; // 아이템 ID
    private String itemName; // 상품명
    private String itemDescription; // 상품 설명
    private String itemUrl; // 상품 URL
    private Long userId; // 사용자 ID
}
