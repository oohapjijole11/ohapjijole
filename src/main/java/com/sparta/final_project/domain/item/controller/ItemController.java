package com.sparta.final_project.domain.item.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController

public class ItemController {

    private final ItemService itemService;


    // 상품 등록
    @PostMapping("/items")
    public ResponseEntity<ItemCreateResponse> createItem(
            @RequestBody ItemCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        // 상품 생성
        ItemCreateResponse itemCreateResponse = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCreateResponse);
    }
    // 상품 조회
    @GetMapping("/itemss/{id}")
    public ResponseEntity<ItemSimpleResponse> getItem(@PathVariable Long id) {
        ItemSimpleResponse itemSimpleResponse = itemService.getItem(id);
        return ResponseEntity.ok(itemSimpleResponse);
    }

    // 상품 수정
    @PutMapping("/items/{id}")
    public ResponseEntity<ItemUpdateResponse> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser

    ) {
        ItemUpdateResponse updatedItem = itemService.updateItem(id, request, authUser);

        // 업데이트된 상품 정보를 담은 응답 반환
        return ResponseEntity.ok(updatedItem);
    }

    // 상품 삭제
    @DeleteMapping("items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();

    }
}
