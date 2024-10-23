package com.sparta.final_project.domain.item.dto.response;

import com.sparta.final_project.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemSaveResponse {
        private Long id;
        private List<String> itemUrls;  // List로 변경
        private String itemName;
        private String itemDescription;
        private Long userId;

        public ItemSaveResponse(Item item) {
            this.id = item.getItemId();
            this.itemUrls = item.getItemUrls();  // List로 처리
            this.itemName = item.getItemName();
            this.itemDescription = item.getItemDescription();
            this.userId = item.getUser().getUserId();
        }



}
