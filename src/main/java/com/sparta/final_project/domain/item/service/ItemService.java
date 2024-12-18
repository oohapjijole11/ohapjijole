package com.sparta.final_project.domain.item.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.item.dto.request.ItemCreateRequest;
import com.sparta.final_project.domain.item.dto.request.ItemUpdateRequest;
import com.sparta.final_project.domain.item.dto.response.ItemCreateResponse;
import com.sparta.final_project.domain.item.dto.response.ItemSimpleResponse;
import com.sparta.final_project.domain.item.dto.response.ItemUpdateResponse;
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    // 상품 생성 (상품 생성 시에는 @Cacheable이 필요X)
    @Transactional
    public ItemCreateResponse createItem(ItemCreateRequest request, AuthUser authUser) {
        // 인증된 사용자의 정보로 User 조회
        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        // 새로운 Item 생성 후 저장
        Item item = new Item(request.getName(), request.getDescription(), request.getImageUrls(), user);
        Item savedItem = itemRepository.save(item);
        // 생성된 Item 정보를 응답 객체로 반환
        return new ItemCreateResponse(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getImageUrls()
        );
    }

    // 상품 조회 (캐시 적용)
    @Transactional
    @Cacheable(value = "items", key = "#id")
    public ItemSimpleResponse getItem(Long id) {
        // ID로 Item 조회 없을 시 예외 발생
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_ITEM));
        // 조회된 Item 정보를 응답 객체로 반환
        return new ItemSimpleResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrls()
        );

    }

    // 상품 수정
    @Transactional
    @CacheEvict(value = "items", key = "#id")
    public ItemUpdateResponse updateItem(Long id, ItemUpdateRequest request) {
        // ID로 Item 조회 및 수정, 없을 시 예외 발생
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_ITEM));
        item.update(request.getName(), request.getDescription(), request.getImageUrls());
        item = itemRepository.save(item);//// 수정된 Item 저장
        // 업데이트된 Item 정보를 응답 객체로 반환
        return new ItemUpdateResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrls(),
                item.getUser().getId()
        );
    }

    // 상품 삭제
    @Transactional
    @CacheEvict(value = "items", key = "#id")
    public void deleteItem(Long id) {
        // ID로 Item 존재 여부 확인 후 삭제 없을 시 예외 발생
        if (!itemRepository.existsById(id)) {
            throw new OhapjijoleException(ErrorCode._NOT_FOUND_ITEM);
        }
        itemRepository.deleteById(id);
    }
}
