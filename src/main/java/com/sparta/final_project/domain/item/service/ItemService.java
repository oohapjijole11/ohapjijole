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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    //상품 생성
    @Transactional
    public ItemCreateResponse createItem(ItemCreateRequest request, AuthUser authUser) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Item item = new Item(request.getName(), request.getDescription(), request.getImageUrl(), user);
        Item savedItem = itemRepository.save(item);

        return new ItemCreateResponse(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getDescription(),
                savedItem.getImageUrl()
        );
    }

    // 상품 조회
    public ItemSimpleResponse getItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new OhapjijoleException(ErrorCode. _ATTACHMENT_NOT_FOUND));
        ItemSimpleResponse itemSimpleResponse = new ItemSimpleResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl()
        );

        return itemSimpleResponse;
    }
    // 상품 수정
    @Transactional
    public ItemUpdateResponse updateItem(Long id, ItemUpdateRequest request) {
        //상품 조회
        Item item = itemRepository.findById(id).orElseThrow(() -> new OhapjijoleException(ErrorCode. _ATTACHMENT_NOT_FOUND));
        item.update(request.getName(),request.getDescription(),request.getImageUrl());
        return new ItemUpdateResponse(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getImageUrl(),
                item.getId());
    }
    // 상품 삭제
    @Transactional
    public void deleteItem(Long id) {
        if(!itemRepository.existsById(id)) {
            throw new OhapjijoleException(ErrorCode. _ATTACHMENT_NOT_FOUND);
        }
        itemRepository.deleteById(id);
    }
}
