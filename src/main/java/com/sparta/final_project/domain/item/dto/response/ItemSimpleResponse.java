package com.sparta.final_project.domain.item.dto.response;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;


@Getter
public class ItemSimpleResponse implements Serializable {

    private final Long id;
    private final String name;
    private final String description;
    private final List<String> imageUrls;

    // 생성자
    public ItemSimpleResponse(Long id, String name, String description, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
    }
}


