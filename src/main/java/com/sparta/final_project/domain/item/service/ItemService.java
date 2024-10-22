package com.sparta.final_project.domain.item.service;

import com.sparta.final_project.domain.common.service.S3Service;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemSaveResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3Service s3Service;

    @Transactional
    public ItemSaveResponse uploadItems(MultipartFile file, String itemName, String itemDescription, Long userId) {
        // userId로 User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // S3에 파일 업로드 후 URL 반환
        String fileUrl = s3Service.uploadFile(file);

        // 새로운 아이템 생성 및 저장
        Item newItem = new Item(itemName, itemDescription, fileUrl, user);
        itemRepository.save(newItem); // DB에 아이템 저장 후 id 생성

        return new ItemSaveResponse(newItem.getItemId(), newItem.getItemName(), newItem.getItemDescription(), fileUrl, user.getUserId());
    }
    // 상품 조회
    @Transactional(readOnly = true) // 읽기 작업이므로 readOnly 설정
    public ItemSimpleResponse getItem(Long itemId) {
        Item item = findItemById(itemId); // DB에서 아이템 조회
        return new ItemSimpleResponse(item.getId(), item.getItemName(), item.getItemDescription(), item.getItemUrl(), item.getUserId());
    }

    @Transactional // 쓰기 작업이므로 트랜잭션을 활성화합니다.
    public ItemUpdateResponse updateItem(Long itemId, MultipartFile file, ItemUpdateRequest itemUpdateRequest) {
        Item existingItem = findItemById(itemId); // DB에서 아이템 조회
        if (existingItem == null) {
            throw new EntityNotFoundException("Item not found with id: " + itemId);
        }

        // S3에서 기존 파일 삭제
        s3Service.deleteFile(existingItem.getItemUrl());

        // 새 파일 유효성 검사
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null or empty");
        }

        // 새 파일 업로드
        String newFileUrl = s3Service.uploadFile(file);

        // 아이템 업데이트
        existingItem.setItemName(itemUpdateRequest.getItemName());
        existingItem.setItemDescription(itemUpdateRequest.getItemDescription());
        existingItem.setItemUrl(newFileUrl);
        saveItem(existingItem); // DB에 업데이트

        return new ItemUpdateResponse(existingItem.getId(), existingItem.getItemName(), existingItem.getItemDescription(), newFileUrl, existingItem.getUserId());
    }

    // 상품 삭제
    @Transactional
    public void deleteItem(Long itemId) {
        Item existingItem = findItemById(itemId);
        s3Service.deleteFile(existingItem.getItemUrl());
        removeItem(existingItem);
    }

    private void saveItem(Item item) {
        itemRepository.save(item);
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.ITEM_NOT_FOUND));
    }

    private void removeItem(Item item) {
        itemRepository.delete(item);
    }
}