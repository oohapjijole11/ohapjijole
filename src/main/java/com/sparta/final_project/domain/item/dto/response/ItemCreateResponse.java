package com.sparta.final_project.domain.item.dto.response;

import com.sparta.final_project.domain.item.entity.Item;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemCreateResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    public ItemCreateResponse(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}
