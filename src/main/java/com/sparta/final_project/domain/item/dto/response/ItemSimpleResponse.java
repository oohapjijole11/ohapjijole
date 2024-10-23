package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.util.List;


@Getter
public class ItemSimpleResponse {
        private Long id; // 아이템 ID
        private String itemName; // 상품명
        private String itemDescription; // 상품 설명
        private List<String> itemUrls; // 상품 URL 목록
        private Long userId; // 사용자 ID

        public ItemSimpleResponse(Long id, String itemName, String itemDescription, List<String> itemUrls, Long userId) {
            this.id = id;
            this.itemName = itemName;
            this.itemDescription = itemDescription;
            this.itemUrls = itemUrls; // 변경: List<String>으로 수정
            this.userId = userId;
        }

    }


