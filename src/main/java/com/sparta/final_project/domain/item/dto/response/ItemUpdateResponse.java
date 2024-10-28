package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.util.List;


@Getter
public class ItemUpdateResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;
    private final Long userId;

    public ItemUpdateResponse(Long id, String name, String description, String imageUrl, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
    }
}
