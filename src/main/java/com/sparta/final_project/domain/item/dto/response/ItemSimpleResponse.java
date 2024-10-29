package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;


@Getter
public class ItemSimpleResponse implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final String imageUrl;

    // 생성자
    public ItemSimpleResponse(Long id, String name, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}


