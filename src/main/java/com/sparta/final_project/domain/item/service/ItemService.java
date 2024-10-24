package com.sparta.final_project.domain.item.service;

import com.sparta.final_project.domain.item.dto.response.ItemSaveResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
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

    // 상품 업로드
    @Transactional // 쓰기 작업이므로 트랜잭션을 활성화합니다.
    public ItemSaveResponse uploadItems(MultipartFile file, String itemName, String itemDescription, Long userId) {
        String fileUrl = s3Service.uploadFile(file);

        Item newItem = new Item(itemName, itemDescription, fileUrl, userId);
        saveItem(newItem); // DB에 아이템 저장

        return new ItemSaveResponse(newItem.getId(), newItem.getItemName(), newItem.getItemDescription(), fileUrl, newItem.getUserId());
    }

    // 상품 조회
    @Transactional(readOnly = true) // 읽기 작업이므로 readOnly 설정
    public ItemSimpleResponse getItem(Long itemId) {
        Item item = findItemById(itemId); // DB에서 아이템 조회
        return new ItemSimpleResponse(item.getId(), item.getItemName(), item.getItemDescription(), item.getItemUrl(), item.getUserId());
    }

    // 상품 수정
    @Transactional // 쓰기 작업이므로 트랜잭션을 활성화합니다.
    public ItemUpdateResponse updateItem(Long itemId, MultipartFile file, ItemUpdateRequest itemUpdateRequest) {
        Item existingItem = findItemById(itemId); // DB에서 아이템 조회

        // S3에서 기존 파일 삭제
        s3Service.deleteFile(existingItem.getItemUrl());

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
    @Transactional // 쓰기 작업이므로 트랜잭션을 활성화합니다.
    public void deleteItem(Long itemId) {
        Item existingItem = findItemById(itemId); // DB에서 아이템 조회
        s3Service.deleteFile(existingItem.getItemUrl()); // S3에서 파일 삭제
        removeItem(existingItem); // DB에서 아이템 삭제
    }

    // DB에 아이템 저장
    private void saveItem(Item item) {
        itemRepository.save(item);
    }

    // 아이템 조회 메서드
    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new QueensTrelloException(ErrorCode.ITEM_NOT_FOUND)); // 예외 처리
    }

    // 아이템 삭제 메서드
    private void removeItem(Item item) {
        itemRepository.delete(item);
    }
}