package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.util.List;


@Getter
public class ItemUpdateResponse {
        private Long id; // 아이템 ID
        private String itemName; // 상품명
        private String itemDescription; // 상품 설명
        private List<String> itemUrls; // 파일 URL 목록
        private Long userId; // 사용자 ID

        // 생성자
        public ItemUpdateResponse(Long id, String itemName, String itemDescription, List<String> itemUrls, Long userId) {
            this.id = id;
            this.itemName = itemName;
            this.itemDescription = itemDescription;
            this.itemUrls = itemUrls;
            this.userId = userId;
        }
}
