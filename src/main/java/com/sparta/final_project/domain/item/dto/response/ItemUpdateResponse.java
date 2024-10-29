package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.util.List;


@Getter
public class ItemUpdateResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final List<String> imageUrls;
    private final Long userId;

    public ItemUpdateResponse(Long id, String name, String description, List<String> imageUrls, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
        this.userId = userId;
    }
}
