package com.sparta.final_project.domain.item.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemUpdateRequest {
    private String itemName;
    private String itemDescription;

}
