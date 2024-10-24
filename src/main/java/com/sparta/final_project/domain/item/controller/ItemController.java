package com.sparta.final_project.domain.item.controller;

import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ItemController {
    private final ItemService itemService;

    // 상품 업로드
    @PutMapping
    public ResponseEntity<String> uploadItems(
            @RequestPart("file") MultipartFile file,
            @RequestPart("itemName") String itemName,
            @RequestPart("itemDescription") String itemDescription,
            @RequestPart("userId") Long userId) {

        String fileUrl = s3Service.uploadFile(file); // S3에 파일 업로드하고 URL 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(fileUrl);
    }

    // 상품 조회
    @GetMapping("/{itemId}") // 아이템 ID를 기반으로 조회
    public ResponseEntity<ItemSimpleResponse> getItem(@PathVariable Long itemId) {
        ItemSimpleResponse itemResponse = itemService.getItem(itemId); // 해당 ID의 아이템 정보를 가져옴
        return ResponseEntity.ok(itemResponse);
    }

    // 상품 수정
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemUpdateResponse> updateItem(
            @PathVariable Long itemId,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("itemUpdateRequest") ItemUpdateRequest itemUpdateRequest) {
        ItemUpdateResponse itemResponse = itemService.updateItem(itemId, file, itemUpdateRequest);
        return ResponseEntity.ok(itemResponse);
    }

     // 상품 삭제
    @DeleteMapping("/{itemId}") // 아이템 ID를 기반으로 삭제
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId); // 아이템 삭제 메서드 호출
        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다."); // 성공 메시지를 문자열로 반환
    }
}