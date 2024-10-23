package com.sparta.final_project.domain.item.controller;

import com.sparta.final_project.domain.common.service.S3Service;
import com.sparta.final_project.domain.item.dto.request.ItemSaveRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemSaveResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.service.ItemService;
import com.sparta.final_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class ItemController {
    private final ItemService itemService;
    private final S3Service s3Service;

    @PostMapping("/item/{userId}")
    public ResponseEntity<ItemSaveResponse> createItem(
            @RequestBody ItemSaveRequest itemSaveRequest,
            @PathVariable Long userId) { // AuthUser 주입
        ItemSaveResponse itemSaveResponse = itemService.createItem(itemSaveRequest, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemSaveResponse);
    }

    @GetMapping("/{itemId}/{userId}")
    public ResponseEntity<ItemSimpleResponse> getItem(@PathVariable Long itemId) {
        ItemSimpleResponse itemSimpleResponse = itemService.getItem(itemId);
        return ResponseEntity.ok(itemSimpleResponse);
    }

    @PutMapping("/{itemId}/{userId}")
    public ResponseEntity<ItemUpdateResponse> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest itemUpdateRequest,
            @PathVariable Long userId) { // AuthUser 주입
        ItemUpdateResponse itemUpdateResponse = itemService.updateItem(itemId, itemUpdateRequest, userId);
        return ResponseEntity.ok(itemUpdateResponse);
    }

    @DeleteMapping("/{itemId}/{userId}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long itemId,
            @PathVariable Long userId) { // AuthUser 주입
//        Long userId = authUser.getUserId();
        itemService.deleteItem(itemId, userId);
        return ResponseEntity.noContent().build();
    }
}