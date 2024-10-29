package com.sparta.final_project.domain.item.dto.response;

import com.sparta.final_project.domain.item.entity.Item;
import lombok.Getter;

import java.util.List;

@Getter
public class ItemCreateResponse {

    private final Long id;
    private final String name;
    private final String description;
    private final List<String> imageUrls;

    public ItemCreateResponse(Long id, String name, String description, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
    }
}

